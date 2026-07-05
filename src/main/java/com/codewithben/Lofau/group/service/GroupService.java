package com.codewithben.Lofau.group.service;

import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
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

}