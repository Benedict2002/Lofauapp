package com.codewithben.Lofau.Post.dto.request;

import com.codewithben.Lofau.Post.enums.Category;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {

    @Size(max = 255)
    private String title;

    @Size(max = 5000)
    private String description;

    private Category category;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private BigDecimal rewardAmount;

    private Boolean anonymous;

}