package com.codewithben.Lofau.User.userService;

import com.codewithben.Lofau.User.dao.request.UpdateUserRequest;
import com.codewithben.Lofau.User.dao.request.UserRequest;
import com.codewithben.Lofau.User.dao.response.UserResponse;
import com.codewithben.Lofau.media.dto.response.MediaResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse getUser(UUID id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(
            UUID id,
            UpdateUserRequest request);
    MediaResponse uploadProfilePhoto(
            MultipartFile file
    ) throws IOException;

    MediaResponse uploadCoverPhoto(
            MultipartFile file
    ) throws IOException;



    void deleteUser(UUID id);
}
