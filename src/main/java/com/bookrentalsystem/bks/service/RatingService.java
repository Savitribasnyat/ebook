package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.model.Rating;

import java.security.Principal;

public interface RatingService {
    Rating saveRating(Long ratingValue, Long bookId, Principal principal);
}
