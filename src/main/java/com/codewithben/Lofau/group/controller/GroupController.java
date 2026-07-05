package com.codewithben.Lofau.group.controller;

import com.codewithben.Lofau.group.dto.request.CreateGroupRequest;
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

}