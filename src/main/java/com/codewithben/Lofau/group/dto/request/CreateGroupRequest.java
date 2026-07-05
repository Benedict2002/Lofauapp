package com.codewithben.Lofau.group.dto.request;

import com.codewithben.Lofau.group.enums.GroupCategory;
import com.codewithben.Lofau.group.enums.GroupVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGroupRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private GroupVisibility visibility;

    @NotNull
    private GroupCategory category;

    private String location;

    private Double latitude;

    private Double longitude;

}