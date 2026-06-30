package com.codewithben.Lofau.User.userService;

import com.codewithben.Lofau.User.dao.request.UpdateUserRequest;
import com.codewithben.Lofau.User.dao.request.UserRequest;
import com.codewithben.Lofau.User.dao.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse getUser(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(
            Long id,
            UpdateUserRequest request);



    void deleteUser(Long id);
}
