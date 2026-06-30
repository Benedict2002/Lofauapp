package com.codewithben.Lofau.User.userService;

import com.codewithben.Lofau.User.dao.request.UpdateUserRequest;
import com.codewithben.Lofau.User.dao.request.UserRequest;
import com.codewithben.Lofau.User.dao.response.UserResponse;
import com.codewithben.Lofau.User.mapper.UserMapper;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.domain.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
   // private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(
            UserRequest request) {

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(request.getPassword())
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBio(request.getBio());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setProfilePictureUrl(
                request.getProfilePictureUrl());

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}
