package com.codewithben.Lofau.User.dao.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private String profilePictureUrl;

    private String bio;

    private Boolean verified;
}
