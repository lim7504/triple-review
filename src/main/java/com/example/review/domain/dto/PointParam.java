package com.example.review.domain.dto;

import com.example.review.domain.PointType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
public class PointParam {

    private String userId;
    private PointType pointType;
    private String reason;
    private Integer point;

    public static PointParam createPointParam(String reviewId, String userId, PointType pointType, boolean review, boolean photo, boolean firstReview) {
        PointParam newPointParam = new PointParam();
        newPointParam.userId = userId;
        newPointParam.pointType = pointType;
        Reason reason = Reason.builder()
                .reviewId(reviewId)
                .review(review)
                .photo(photo)
                .firstReview(firstReview)
                .build();
        newPointParam.reason = reason.toString();
        newPointParam.point = (review ? 1 : 0) + (photo ? 1 : 0) + (firstReview ? 1 : 0);
        return newPointParam;
    }

    @ToString
    static class Reason {
        private String reviewId;
        private boolean review;
        private boolean photo;
        private boolean firstReview;

        @Builder
        public Reason(String reviewId, boolean review, boolean photo, boolean firstReview) {
            this.reviewId = reviewId;
            this.review = review;
            this.photo = photo;
            this.firstReview = firstReview;
        }
    }


}
