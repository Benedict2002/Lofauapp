package com.codewithben.Lofau.Auth.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private String phoneNumber;

}
