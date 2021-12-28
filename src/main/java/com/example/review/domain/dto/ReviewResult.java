package com.example.review.domain.dto;

import lombok.Getter;

@Getter
public class ReviewResult {

    private String reviewId;

    public static ReviewResult createReviewResult(String reviewId) {
        ReviewResult newReviewResult = new ReviewResult();
        newReviewResult.reviewId = reviewId;
        return newReviewResult;
    }
}
