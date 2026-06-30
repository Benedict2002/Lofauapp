package com.codewithben.Lofau.Auth.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    private String token;

    private Long userId;

    private String username;

}
