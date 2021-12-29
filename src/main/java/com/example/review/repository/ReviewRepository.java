package com.example.review.repository;

import com.example.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    Optional<Review> findByIdAndDelYn(String id, Boolean delYn);

    boolean existsByUserIdAndPlaceIdAndDelYn(String userId, String placeId, Boolean delYn);

    boolean existsByPlaceIdAndFirstReviewYn(String placeId, Boolean firstReviewYn);

}
