package com.codewithben.Lofau.group.dto.response;

import com.codewithben.Lofau.group.enums.GroupRole;
import com.codewithben.Lofau.group.enums.MemberStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupMemberResponse {

    private Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private String profileImage;

    private GroupRole role;

    private MemberStatus status;

    private LocalDateTime joinedAt;

}