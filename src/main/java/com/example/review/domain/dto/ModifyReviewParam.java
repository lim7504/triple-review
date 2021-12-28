package com.example.review.domain.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ModifyReviewParam {

    @Size(min = 36, max = 36)
    private String userId;

    @NotNull
    @Size(min = 1)
    private String content;

    private List<@Size(min = 36, max = 36) String> attachedPhotoIds = new ArrayList<>();

}
