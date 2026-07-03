package com.codewithben.Lofau.Post.dto.request;

import com.codewithben.Lofau.Post.enums.Category;
import com.codewithben.Lofau.Post.enums.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private PostType postType;

    @NotNull
    private Category category;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private BigDecimal rewardAmount;

    private Boolean anonymous = false;

}