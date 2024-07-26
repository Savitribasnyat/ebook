package com.bookrentalsystem.bks.service.ratingserviceimpl;

import com.bookrentalsystem.bks.model.Book;
import com.bookrentalsystem.bks.model.Rating;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.BookRepo;
import com.bookrentalsystem.bks.repo.RatingRepo;
import com.bookrentalsystem.bks.repo.UserRepo;
import com.bookrentalsystem.bks.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepo ratingRepo;
    private final UserRepo userRepo;
    private final BookRepo bookRepo;
    @Override
    public Rating saveRating(Long ratingValue, Long bookId,Principal principal) {
        String loginUser = principal.getName();
        Optional<User> userOptional = userRepo.findUserByEmail(loginUser);
        Optional<Book> book =  bookRepo.findById(bookId);
        Optional<Rating> ratingOptional = ratingRepo.findRatingByUserAndBook(userOptional.get().getId(),bookId);
        if(ratingOptional.isPresent()){
            Rating rating = ratingOptional.get();
            rating.setRatingNumber(ratingValue);
            ratingRepo.save(rating);
            return rating;
        }
        Rating rating =new Rating();
        rating.setUser(userOptional.get());
        rating.setBook(book.get());
        rating.setRatingNumber(ratingValue);
        ratingRepo.save(rating);
        return rating;
    }
}
