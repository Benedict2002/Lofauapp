package com.codewithben.Lofau.Auth.controller;

import com.codewithben.Lofau.Auth.dto.request.LoginRequest;
import com.codewithben.Lofau.Auth.dto.request.RegisterRequest;
import com.codewithben.Lofau.Auth.dto.response.ApiResponse;
import com.codewithben.Lofau.Auth.dto.response.AuthenticationResponse;
import com.codewithben.Lofau.Auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {

        AuthenticationResponse response =
                authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<AuthenticationResponse>builder()
                                .success(true)
                                .message("User registered successfully")
                                .data(response)
                                .build()
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody LoginRequest request
    ) {

        AuthenticationResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthenticationResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

}
