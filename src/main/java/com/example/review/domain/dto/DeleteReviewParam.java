package com.example.review.domain.dto;

import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class DeleteReviewParam {

    @Size(min = 36, max = 36)
    private String userId;

}
