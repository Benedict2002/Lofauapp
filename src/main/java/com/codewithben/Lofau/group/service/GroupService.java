package com.codewithben.Lofau.group.service;

import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
import com.codewithben.Lofau.group.dto.request.UpdateGroupRequest;
import com.codewithben.Lofau.group.dto.response.GroupMemberResponse;
import com.codewithben.Lofau.group.dto.response.GroupResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            Long userId
    );

    void rejectJoinRequest(
            String groupId,
            Long userId
    );
    void promoteToAdmin(
            String groupId,
            Long userId
    );

    void demoteAdmin(
            String groupId,
            Long userId
    );

    void removeMember(
            String groupId,
            Long userId
    );
    GroupResponse updateGroup(
            String groupId,
            UpdateGroupRequest request
    );
    void deleteGroup(String groupId);

}