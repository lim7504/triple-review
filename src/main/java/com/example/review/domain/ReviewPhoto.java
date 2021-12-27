package com.example.review.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
public class ReviewPhoto extends CreatedAuditing{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String imageId;

    private Boolean delYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;


    public static ReviewPhoto createReviewPhoto(Review review, String imageId) {
        ReviewPhoto newReviewPhoto = new ReviewPhoto();
        newReviewPhoto.review = review;
        newReviewPhoto.imageId = imageId;
        newReviewPhoto.delYn = false;
        return newReviewPhoto;
    }

    public void deleteReviewPhoto() {
        this.delYn = true;
    }
}
