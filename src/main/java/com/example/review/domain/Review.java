package com.example.review.domain;

import com.example.review.domain.dto.AddReviewParam;
import com.example.review.domain.dto.ModifyReviewParam;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
public class Review extends CreatedModifiedAuditing{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String userId;

    private String placeId;

    private String content;

    private Boolean delYn;

    private Boolean firstReviewYn;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewPhoto> reviewPhotos = new ArrayList<>();

    // 리뷰 추가
    public static Review createReview(AddReviewParam addReviewParam) {
        Review newReview = new Review();
        newReview.placeId = addReviewParam.getPlaceId();
        newReview.userId = addReviewParam.getUserId();
        newReview.content = addReviewParam.getContent();
        newReview.delYn = false;
        newReview.firstReviewYn = false;
        for (String attachedPhotoId : addReviewParam.getAttachedPhotoIds()) {
            ReviewPhoto reviewPhoto = ReviewPhoto.createReviewPhoto(newReview, attachedPhotoId);
            newReview.reviewPhotos.add(reviewPhoto);
        }
        return newReview;
    }

    // 리뷰 변경
    public void changeReview(ModifyReviewParam modifyReviewParam) {
        this.content = modifyReviewParam.getContent();
        this.getUsedReviewPhoto().forEach(ReviewPhoto::deleteReviewPhoto);
        for (String attachedPhotoId : modifyReviewParam.getAttachedPhotoIds()) {
            ReviewPhoto reviewPhoto = ReviewPhoto.createReviewPhoto(this, attachedPhotoId);
            this.reviewPhotos.add(reviewPhoto);
        }
    }

    // 리뷰 삭제
    public void deleteReview() {
        this.delYn = true;
        this.firstReviewYn = false;
        this.getUsedReviewPhoto().forEach(ReviewPhoto::deleteReviewPhoto);
    }

    // 사용중인 리뷰 사진만 조회
    public List<ReviewPhoto> getUsedReviewPhoto() {
        return this.reviewPhotos.stream()
                .filter(reviewPhoto -> !reviewPhoto.getDelYn())
                .collect(Collectors.toList());
    }

    public int getUsedReviewPhotoCnt() {
        return this.getUsedReviewPhoto().size();
    }
}
