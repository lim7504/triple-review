package com.example.review.service;

import com.example.review.client.PointServiceClient;
import com.example.review.config.Code;
import com.example.review.config.ResponseResult;
import com.example.review.config.TripleException;
import com.example.review.domain.PointType;
import com.example.review.domain.Review;
import com.example.review.domain.dto.AddReviewParam;
import com.example.review.domain.dto.ModifyReviewParam;
import com.example.review.domain.dto.PointParam;
import com.example.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PointServiceClient pointServiceClient;

    // 리뷰 추가
    @Transactional
    public ResponseResult<Object> addReview(AddReviewParam addReviewParam) {
        this.checkAlreadyReviewInPlace(addReviewParam.getUserId(), addReviewParam.getPlaceId());
        Review review = Review.createReview(addReviewParam);
        Review savedReview = this.reviewRepository.save(review);

        boolean existFirstReviewInPlace = this.existFirstReviewInPlace(addReviewParam.getPlaceId());
        boolean existsPhoto = review.getUsedReviewPhotoCnt() > 0;
        PointParam pointParam =
                PointParam.createPointParam(savedReview.getId(), addReviewParam.getUserId(), PointType.ADD, true, existsPhoto, existFirstReviewInPlace);
        return this.pointServiceClient.postPoint(pointParam);
    }

    // 리뷰 수정
    @Transactional
    public ResponseResult<Object> modifyReview(String reviewId, ModifyReviewParam modifyReviewParam) {
        ResponseResult<Object> result = ResponseResult.ok();
        Review review = this.getReview(reviewId);
        boolean beforeExistsPhoto = review.getUsedReviewPhotoCnt() > 0;
        review.changeReview(modifyReviewParam);
        boolean afterExistsPhoto = review.getUsedReviewPhotoCnt() > 0;

        PointParam pointParam = null;
        if(!beforeExistsPhoto && afterExistsPhoto) {
            pointParam = PointParam.createPointParam(review.getId(), review.getUserId(), PointType.ADD, false, true, false);
        } else if (beforeExistsPhoto && !afterExistsPhoto) {
            pointParam = PointParam.createPointParam(review.getId(), review.getUserId(), PointType.DELETE, false, true, false);
        }
        if(pointParam != null) {
            result = this.pointServiceClient.postPoint(pointParam);
        }
        return result;
    }

    // 리뷰 삭제
    @Transactional
    public ResponseResult<Object> deleteReview(String reviewId) {
        Review review = this.getReview(reviewId);
        boolean existsPhoto = review.getUsedReviewPhotoCnt() > 0;
        boolean firstReviewYn = review.getFirstReviewYn();
        review.deleteReview();
        PointParam pointParam =
                PointParam.createPointParam(review.getId(), review.getUserId(), PointType.DELETE, true, existsPhoto, firstReviewYn);
        return this.pointServiceClient.postPoint(pointParam);
    }

    private Review getReview(String reviewId) {
        return this.reviewRepository.findByIdAndDelYn(reviewId, false)
                .orElseThrow(() -> new TripleException(Code.REVIEW_NOT_FOUND));
    }

    private void checkAlreadyReviewInPlace(String userId, String placeId) {
        boolean existReviewByUser
                = this.reviewRepository.existsByUserIdAndPlaceId(userId, placeId);

        if(existReviewByUser)
            throw new TripleException(Code.ALREADY_REVIEW_IN_PLACE);
    }

    private boolean existFirstReviewInPlace(String placeId) {
        return this.reviewRepository.existsByPlaceIdAndFirstReviewYn(placeId, true);
    }

}
