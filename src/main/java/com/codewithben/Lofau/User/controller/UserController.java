package com.codewithben.Lofau.User.controller;

import com.codewithben.Lofau.User.dao.request.UpdateUserRequest;
import com.codewithben.Lofau.User.dao.request.UserRequest;
import com.codewithben.Lofau.User.dao.response.UserResponse;
import com.codewithben.Lofau.User.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse createUser(
            @RequestBody UserRequest request) {

        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(
            @PathVariable Long id) {

        return userService.getUser(id);
    }

    @GetMapping
    public List<UserResponse> getUsers() {

        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);
    }
}
