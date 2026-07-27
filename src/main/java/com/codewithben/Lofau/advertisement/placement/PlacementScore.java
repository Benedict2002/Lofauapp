package com.codewithben.Lofau.advertisement.placement;

import com.codewithben.Lofau.advertisement.entity.Advertisement;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlacementScore {

    private Advertisement advertisement;

    private double score;
}