package com.codewithben.Lofau.User.dao.request;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private String phoneNumber;
}
