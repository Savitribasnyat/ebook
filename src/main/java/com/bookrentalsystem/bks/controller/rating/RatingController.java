package com.bookrentalsystem.bks.controller.rating;

import com.bookrentalsystem.bks.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/save/{ratingValue}/{bookId}")
    public String saveRating(@PathVariable Long ratingValue,@PathVariable Long bookId,Principal principal){
        ratingService.saveRating(ratingValue,bookId,principal);
        return "redirect:/book/view/"+bookId;
    }
}
