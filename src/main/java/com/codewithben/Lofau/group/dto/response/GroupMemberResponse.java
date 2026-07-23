package com.codewithben.Lofau.group.dto.response;

import com.codewithben.Lofau.group.enums.GroupRole;
import com.codewithben.Lofau.group.enums.MemberStatus;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GroupMemberResponse {

    private UUID userId;

    private String username;

    private String firstName;

    private String lastName;

    private MediaResponse profileImage;

    private MediaResponse coverImage;

    private GroupRole role;

    private MemberStatus status;

    private Boolean verified;

    private LocalDateTime joinedAt;
}