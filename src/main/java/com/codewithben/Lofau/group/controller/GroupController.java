package com.codewithben.Lofau.group.controller;

import com.codewithben.Lofau.Auth.dto.response.ApiResponse;
import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
import com.codewithben.Lofau.group.dto.request.UpdateGroupRequest;
import com.codewithben.Lofau.group.dto.response.GroupMemberResponse;
import com.codewithben.Lofau.group.dto.response.GroupResponse;
import com.codewithben.Lofau.group.enums.GroupCategory;
import com.codewithben.Lofau.group.enums.GroupVisibility;
import com.codewithben.Lofau.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GroupResponse> createGroup(

            @RequestParam("name") String name,

            @RequestParam("description") String description,

            @RequestParam("visibility") GroupVisibility visibility,

            @RequestParam("category") GroupCategory category,

            @RequestParam(value = "location", required = false) String location,

            @RequestParam(value = "latitude", required = false) Double latitude,

            @RequestParam(value = "longitude", required = false) Double longitude,

            @RequestPart(value = "profileImage", required = false)
            MultipartFile profileImage,

            @RequestPart(value = "coverImage", required = false)
            MultipartFile coverImage

    ) throws IOException {

        CreateGroupRequest request = new CreateGroupRequest();

        request.setName(name);
        request.setDescription(description);
        request.setVisibility(visibility);
        request.setCategory(category);
        request.setLocation(location);
        request.setLatitude(latitude);
        request.setLongitude(longitude);

        GroupResponse response = groupService.createGroup(
                request,
                profileImage,
                coverImage
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {

        return ResponseEntity.ok(
                groupService.getAllGroups()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                groupService.getGroupById(id)
        );
    }


    @PostMapping("/{groupId}/join")
    public ResponseEntity<GroupResponse> joinGroup(
            @PathVariable String groupId
    ) {

        return ResponseEntity.ok(
                groupService.joinGroup(groupId)
        );
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable String groupId
    ) {

        groupService.leaveGroup(groupId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> getMembers(
            @PathVariable String groupId
    ) {

        return ResponseEntity.ok(
                groupService.getMembers(groupId)
        );
    }

    @GetMapping("/{groupId}/requests")
    public ResponseEntity<List<GroupMemberResponse>> getPendingRequests(
            @PathVariable String groupId
    ) {

        return ResponseEntity.ok(
                groupService.getPendingRequests(groupId)
        );

    }

    @PutMapping("/{groupId}/requests/{userId}/approve")
    public ResponseEntity<String> approveRequest(

            @PathVariable String groupId,

            @PathVariable UUID userId

    ) {

        groupService.approveJoinRequest(groupId, userId);

        return ResponseEntity.ok("Join request approved.");

    }

    @DeleteMapping("/{groupId}/requests/{userId}/reject")
    public ResponseEntity<String> rejectRequest(

            @PathVariable String groupId,

            @PathVariable UUID userId

    ) {

        groupService.rejectJoinRequest(groupId, userId);

        return ResponseEntity.ok("Join request rejected.");

    }

    @PutMapping("/{groupId}/members/{userId}/promote")
    public ResponseEntity<String> promoteMember(
            @PathVariable String groupId,
            @PathVariable UUID userId
    ) {

        groupService.promoteToAdmin(groupId, userId);

        return ResponseEntity.ok("Member promoted to ADMIN.");

    }
    @PutMapping("/{groupId}/members/{userId}/demote")
    public ResponseEntity<String> demoteAdmin(
            @PathVariable String groupId,
            @PathVariable UUID userId
    ) {

        groupService.demoteAdmin(groupId, userId);

        return ResponseEntity.ok("Admin demoted to MEMBER.");

    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<String> removeMember(
            @PathVariable String groupId,
            @PathVariable UUID userId
    ) {

        groupService.removeMember(groupId, userId);

        return ResponseEntity.ok("Member removed successfully.");

    }


    @PutMapping(
            value = "/{groupId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<GroupResponse> updateGroup(

            @PathVariable String groupId,

            @RequestPart("data") UpdateGroupRequest request,

            @RequestPart(value = "profileImage", required = false)
            MultipartFile profileImage,

            @RequestPart(value = "coverImage", required = false)
            MultipartFile coverImage

    ) throws IOException {

        return ResponseEntity.ok(

                groupService.updateGroup(
                        groupId,
                        request,
                        profileImage,
                        coverImage
                )

        );
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(
            @PathVariable String groupId
    ) {

        groupService.deleteGroup(groupId);

        return ResponseEntity.ok("Group deleted successfully.");

    }
    @PostMapping("/{groupId}/posts/{postId}/pin")
    public ApiResponse<PostResponse> pinPost(
            @PathVariable UUID groupId,
            @PathVariable UUID postId
    ) {

        return ApiResponse.success(
                "Post pinned successfully.",
                groupService.pinPost(groupId, postId)
        );
    }

    @DeleteMapping("/{groupId}/posts/{postId}/pin")
    public ApiResponse<PostResponse> unpinPost(
            @PathVariable UUID groupId,
            @PathVariable UUID postId
    ) {

        return ApiResponse.success(
                "Post unpinned successfully.",
                groupService.unpinPost(groupId, postId)
        );
    }


}