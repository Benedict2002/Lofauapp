package com.codewithben.Lofau.User.dao.response;

import com.codewithben.Lofau.media.dto.response.MediaResponse;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private String bio;

    private Boolean verified;

    private MediaResponse profileImage;

    private MediaResponse coverImage;

}