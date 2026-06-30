package com.codewithben.Lofau.User.mapper;

import com.codewithben.Lofau.User.dao.response.UserResponse;
import com.codewithben.Lofau.User.model.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profilePictureUrl(user.getProfilePictureUrl())
                .bio(user.getBio())
                .verified(user.getVerified())
                .build();
    }
}
