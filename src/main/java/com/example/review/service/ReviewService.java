package com.example.review.service;

import com.example.review.client.PointServiceClient;
import com.example.review.config.Code;
import com.example.review.config.TripleException;
import com.example.review.domain.PointType;
import com.example.review.domain.Review;
import com.example.review.domain.dto.*;
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
    public ReviewResult addReview(AddReviewParam addReviewParam) {
        this.checkAlreadyReviewInPlace(addReviewParam.getUserId(), addReviewParam.getPlaceId());
        boolean existFirstReviewInPlace = this.existFirstReviewInPlace(addReviewParam.getPlaceId());
        Review review = Review.createReview(addReviewParam, existFirstReviewInPlace);
        Review savedReview = this.reviewRepository.save(review);

        boolean existsPhoto = review.getUsedReviewPhotoCnt() > 0;
        PointParam pointParam =
                PointParam.createPointParam(savedReview.getId(), addReviewParam.getUserId(), PointType.ADD, true, existsPhoto, !existFirstReviewInPlace);
        this.pointServiceClient.postPoint(pointParam);
        return ReviewResult.createReviewResult(savedReview.getId());
    }

    // 리뷰 수정
    @Transactional
    public void modifyReview(String reviewId, ModifyReviewParam modifyReviewParam) {
        Review review = this.getReview(reviewId);
        if(!review.getUserId().equals(modifyReviewParam.getUserId())) {
            throw new TripleException(Code.NOT_WRITER_OF_THE_REVIEW);
        }
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
            this.pointServiceClient.postPoint(pointParam);
        }
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(String reviewId, DeleteReviewParam deleteReviewParam) {
        Review review = this.getReview(reviewId);
        if(!review.getUserId().equals(deleteReviewParam.getUserId())) {
            throw new TripleException(Code.NOT_WRITER_OF_THE_REVIEW);
        }
        boolean existsPhoto = review.getUsedReviewPhotoCnt() > 0;
        boolean firstReviewYn = review.getFirstReviewYn();
        review.deleteReview();
        PointParam pointParam =
                PointParam.createPointParam(review.getId(), review.getUserId(), PointType.DELETE, true, existsPhoto, firstReviewYn);
        this.pointServiceClient.postPoint(pointParam);
    }

    // 리뷰 조회
    private Review getReview(String reviewId) {
        return this.reviewRepository.findByIdAndDelYn(reviewId, false)
                .orElseThrow(() -> new TripleException(Code.REVIEW_NOT_FOUND));
    }

    // 이미 해당 지역에 리뷰를 했는지 확인
    private void checkAlreadyReviewInPlace(String userId, String placeId) {
        boolean existReviewByUser
                = this.reviewRepository.existsByUserIdAndPlaceId(userId, placeId);

        if(existReviewByUser)
            throw new TripleException(Code.ALREADY_REVIEW_IN_PLACE);
    }

    // 해당 지역에 첫번째 리뷰가 존재하는지 확인
    private boolean existFirstReviewInPlace(String placeId) {
        return this.reviewRepository.existsByPlaceIdAndFirstReviewYn(placeId, true);
    }

}
