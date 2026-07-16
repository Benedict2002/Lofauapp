package com.codewithben.Lofau.Auth.service;

import com.codewithben.Lofau.Auth.dto.request.LoginRequest;
import com.codewithben.Lofau.Auth.dto.request.RegisterRequest;
import com.codewithben.Lofau.Auth.dto.response.AuthenticationResponse;
import com.codewithben.Lofau.Auth.jwt.JwtService;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.domain.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .accountStatus(AccountStatus.ACTIVE)
                .verified(false)
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }


    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .username(user.getDisplayUsername())
                .build();
    }

}