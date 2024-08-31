package com.example.authenticationjwt.service;

import com.example.authenticationjwt.entity.Role;
import com.example.authenticationjwt.entity.User;
import com.example.authenticationjwt.exception.AppException;
import com.example.authenticationjwt.exception.ErrorCode;
import com.example.authenticationjwt.mapper.UserMapper;
import com.example.authenticationjwt.repository.RoleRepository;
import com.example.authenticationjwt.repository.UserRepository;
import dto.request.user.UserCreationRequest;
import dto.request.user.UserChangePasswordRequest;
import dto.request.user.UserUpdateRequest;
import dto.request.user.UserUpdateRoleRequest;
import dto.response.user.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    SecurityContext context = SecurityContextHolder.getContext();

    String[] ROLE_DEFAULT = {"USER"};

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(userCreationRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleRepository.findAllById(Arrays.asList(ROLE_DEFAULT));
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        String username = context.getAuthentication().getName();
        userUpdateRequest.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        User user = userRepository.findByUsername(username);
        userMapper.updateUser(user, userUpdateRequest);
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(userUpdateRequest.getRoles()));
        user.setRoles(roles);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
    public UserResponse updateRoles(UserUpdateRoleRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if(user != null) {
            Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(request.getRoles()));
            Set<Role> oldRoles = user.getRoles();
            oldRoles.addAll(newRoles);
            user.setRoles(newRoles);
            return userMapper.toUserResponse(userRepository.save(user));
        } else
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }

    public UserResponse changePassword(UserChangePasswordRequest request) {
        String username = context.getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(user.getPassword(), request.getOldPassword())) {
            userRepository.updatePassword(username, request.getNewPassword());
            return userMapper.toUserResponse(user);
        } else
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
    }


    @PostAuthorize("returnObject.username == authentication.name" )
    public UserResponse getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        } else {
            return userMapper.toUserResponse(user.get());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        User user = userRepository.findByUsername(context.getAuthentication().getName());
        return userMapper.toUserResponse(user);
    }
}
