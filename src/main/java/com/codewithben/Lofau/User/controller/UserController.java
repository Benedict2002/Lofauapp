package com.codewithben.Lofau.User.controller;

import com.codewithben.Lofau.User.dao.request.UpdateUserRequest;
import com.codewithben.Lofau.User.dao.request.UserRequest;
import com.codewithben.Lofau.User.dao.response.UserResponse;
import com.codewithben.Lofau.User.userService.UserService;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(
            value = "/profile-photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<MediaResponse> uploadProfilePhoto(
            @RequestParam MultipartFile file
    ) throws IOException {

        return ResponseEntity.ok(
                userService.uploadProfilePhoto(file)
        );
    }

    @PostMapping(
            value = "/cover-photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<MediaResponse> uploadCoverPhoto(
            @RequestParam MultipartFile file
    ) throws IOException {

        return ResponseEntity.ok(
                userService.uploadCoverPhoto(file)
        );
    }

    @PostMapping
    public UserResponse createUser(
            @RequestBody UserRequest request) {

        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(
            @PathVariable UUID id) {

        return userService.getUser(id);
    }

    @GetMapping
    public List<UserResponse> getUsers() {

        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request) {

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable UUID id) {

        userService.deleteUser(id);
    }
}
