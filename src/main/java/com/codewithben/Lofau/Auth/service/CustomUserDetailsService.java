package com.codewithben.Lofau.Auth.service;

import com.codewithben.Lofau.Exception.user.UserException;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UserException {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserException("User not found"));
    }
}
