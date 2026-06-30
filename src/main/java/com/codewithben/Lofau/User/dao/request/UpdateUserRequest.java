package com.codewithben.Lofau.User.dao.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;

    private String lastName;

    private String bio;

    private String profilePictureUrl;

    private String phoneNumber;

}
