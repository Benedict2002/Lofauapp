package com.codewithben.Lofau.group.service;

import com.codewithben.Lofau.Post.dto.response.PostResponse;
import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
import com.codewithben.Lofau.group.dto.request.UpdateGroupRequest;
import com.codewithben.Lofau.group.dto.response.GroupMemberResponse;
import com.codewithben.Lofau.group.dto.response.GroupResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface GroupService {

    GroupResponse createGroup(
            CreateGroupRequest request,
            MultipartFile profileImage,
            MultipartFile coverImage
    ) throws IOException;

    List<GroupResponse> getAllGroups();

    GroupResponse getGroupById(String id);
    GroupResponse joinGroup(String groupId);

    void leaveGroup(String groupId);

    List<GroupMemberResponse> getMembers(String groupId);
    List<GroupMemberResponse> getPendingRequests(String groupId);

    void approveJoinRequest(
            String groupId,
            UUID userId
    );

    void rejectJoinRequest(
            String groupId,
            UUID userId
    );
    void promoteToAdmin(
            String groupId,
            UUID userId
    );

    void demoteAdmin(
            String groupId,
            UUID userId
    );

    void removeMember(
            String groupId,
            UUID userId
    );

     GroupResponse updateGroup(
            String groupId,
            UpdateGroupRequest request,
            MultipartFile profileImage,
            MultipartFile coverImage
    ) throws IOException ;

    PostResponse pinPost(
            UUID groupId,
            UUID postId
    );

    PostResponse unpinPost(
            UUID groupId,
            UUID postId
    );
    void deleteGroup(String groupId);

}