package com.bookrentalsystem.bks.filteringalgorithm;

import com.bookrentalsystem.bks.enums.RoleName;
import com.bookrentalsystem.bks.exception.globalexception.UserHavingThisEmailNotExist;
import com.bookrentalsystem.bks.model.Rating;
import com.bookrentalsystem.bks.model.login.User;
import com.bookrentalsystem.bks.repo.RatingRepo;
import com.bookrentalsystem.bks.repo.UserRepo;
import com.bookrentalsystem.bks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
@RequiredArgsConstructor
public class CollaborativeFiltering {
    private final RatingRepo ratingRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private Map<Long, Map<Long, Float>> userRatings = new HashMap<>();

    //adding user-book rating
    public void adduserRating() {
        userRatings.clear();
        List<Rating> ratingList = ratingRepo.findAll();
        if (!ratingList.isEmpty()) {
            for (Rating eachUserRatingBook : ratingList) {
                if (userRatings.containsKey(eachUserRatingBook.getUser().getId()) || Boolean.TRUE.equals(isAdmin(eachUserRatingBook.getUser().getId()))) {
                    continue;
                }
                List<Rating> ratings = ratingRepo.findRatingByUser(eachUserRatingBook.getUser().getId());
                Map<Long, Float> localRatings = new HashMap<>();
                for (Rating eachRating : ratings) {
                    localRatings.put(eachRating.getBook().getId(), eachRating.getRatingNumber()); //bookId & rating of the book
                }
                userRatings.put(eachUserRatingBook.getUser().getId(), localRatings); // userid,(bookId,rating)
            }
        }
    }


    //calculate similarity between two users using cosine similarity
    private double calculateSimilarity(Long user1, Long user2) {
        //used set because it does not store duplicate value
        Set<Long> user1BooksId = userRatings.get(user1).keySet();    //gives book id
        Set<Long> user2BooksId = userRatings.get(user2).keySet();
        float dotProduct = 0;
        double user1Norm = 0;
        double user2Norm = 0;
        for (Long bookId : user1BooksId) {
            /**
             * If the user 2 also have the given rating to same book then
             * calculating dotProduct
             * dotProduct - 0
             * userRatings.get(user1).get(bookId) - it gives rating number or value
             * multiply that rating value with user2 rating value
             */
            if (user2BooksId.contains(bookId)) {
                dotProduct = dotProduct + userRatings.get(user1).get(bookId) * userRatings.get(user2).get(bookId);
            }
            user1Norm = user1Norm + Math.pow(userRatings.get(user1).get(bookId), 2);  //Norm or magnitude
        }
        for (Long bookId : user2BooksId) {
            user2Norm = user2Norm + Math.pow(userRatings.get(user2).get(bookId), 2);
        }

        if (user1Norm == 0 || user2Norm == 0) {
            return 0.0;//return 0 if one of the user has no ratings
        }
        return dotProduct / (Math.sqrt(user1Norm) * Math.sqrt(user2Norm));
    }

    //Recommend top-k book for the given user
    public List<Long> recommendBooks(Long loginUser, int numRecommendation) {
        adduserRating();   //adding the user-book,rating
        //login user hasn't given the rating to book
        if (!userRatings.containsKey(loginUser)) {
            System.out.println("User " + loginUser + " not found");
            return Collections.emptyList();
        }

        //Calculate similarity between the login user and other users
        Map<Long, Double> similarityMap = new HashMap<>();
        for (Map.Entry<Long, Map<Long, Float>> entry : userRatings.entrySet()) { //using entrySet it list all the data in list format [ ., ., . ]
            Long userId = entry.getKey();  //all the other userId from the userRating set
            if (!userId.equals(loginUser)) {   //calculate the cosine similarity except login user
                double similarity = calculateSimilarity(loginUser, userId);
                similarityMap.put(userId, similarity);    //userId - other users , similarity - cosine calculated value
            }
        }

        //sort users by similarity in descending order
        List<Map.Entry<Long, Double>> sortedSimilarities = new ArrayList<>(similarityMap.entrySet());
        sortedSimilarities.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        //Generate book recommendations based on similar users
        Set<Long> loginUserRatedBookId = userRatings.get(loginUser).keySet();   //all book which the current user have rated
        List<Long> recommendations = new ArrayList<>();

        for (Map.Entry<Long, Double> entry : sortedSimilarities) {   //otherUserId,cosine Value
            Long otherSimilarUser = entry.getKey();

            /**
             * Other similar user rating
             * Map<Long,Float> - bookId,ratingValue
             */
            Map<Long, Float> similarUserRatings = userRatings.get(otherSimilarUser);
            for (Long bookId : similarUserRatings.keySet()) {  //similarUserRating.keySet will give the book id
                /**
                 * check if the login user already ratted book isn't recommend & book should be always unique
                 */
                if (!loginUserRatedBookId.contains(bookId) && !recommendations.contains(bookId)) {
                    recommendations.add(bookId);
                    if (recommendations.size() >= numRecommendation) {
                        return recommendations;
                    }
                }
            }
        }
        return recommendations;
    }

    private Boolean isAdmin(Long userId){
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new UserHavingThisEmailNotExist("User does not exist"));
        if(RoleName.ADMIN.equals(user.getRoles())){
            return true;
        }
        return false;
    }

}
