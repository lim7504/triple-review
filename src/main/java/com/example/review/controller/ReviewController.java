package com.example.review.controller;

import com.example.review.config.ResponseResult;
import com.example.review.domain.dto.AddReviewParam;
import com.example.review.domain.dto.ModifyReviewParam;
import com.example.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 추가
    @PostMapping
    public ResponseEntity addReview(@Validated @RequestBody AddReviewParam addReviewParam) {
        return this.reviewService.addReview(addReviewParam).createResponseEntity();
    }

    // 리뷰 수정
    @PutMapping("/{review-id}")
    public ResponseEntity modifyReview(@PathVariable("review-id") String reviewId,
                                       @Validated @RequestBody ModifyReviewParam modifyReviewParam) {
        return this.reviewService.modifyReview(reviewId, modifyReviewParam).createResponseEntity();
    }

    // 리뷰 삭제
    @DeleteMapping("/{review-id}")
    public ResponseEntity deleteReview(@PathVariable("review-id") String reviewId) {
        return this.reviewService.deleteReview(reviewId).createResponseEntity();
    }
}
