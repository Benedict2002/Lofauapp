package com.codewithben.Lofau.User.mapper;

import com.codewithben.Lofau.User.dao.response.UserResponse;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.media.enums.OwnerType;
import com.codewithben.Lofau.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final MediaService mediaService;

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getDisplayUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .bio(user.getBio())
                .verified(user.getVerified())
                .profileImage(
                        mediaService.getProfile(
                                user.getId(),
                                OwnerType.USER
                        )
                )
                .coverImage(
                        mediaService.getCover(
                                user.getId(),
                                OwnerType.USER
                        )
                )
                .build();
    }
}