package com.codewithben.Lofau.group.dto.request;

import com.codewithben.Lofau.group.enums.GroupCategory;
import com.codewithben.Lofau.group.enums.GroupVisibility;
import lombok.Data;

@Data
public class UpdateGroupRequest {

    private String name;

    private String description;

    private GroupVisibility visibility;

    private GroupCategory category;

    private String location;

    private Double latitude;

    private Double longitude;

    /*
     * Contact
     */
    private String website;

    private String email;

    private String phoneNumber;

    /*
     * Rules
     */
    private String rules;

    /*
     * Settings
     */
    private Boolean allowMemberPosts;

    private Boolean requirePostApproval;

    private Boolean allowMemberInvites;
}