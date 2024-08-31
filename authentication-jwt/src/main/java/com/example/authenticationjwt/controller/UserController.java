package com.example.authenticationjwt.controller;
import com.example.authenticationjwt.service.UserService;
import dto.request.user.UserChangePasswordRequest;
import dto.request.user.UserCreationRequest;
import dto.request.user.UserUpdateRequest;
import dto.request.user.UserUpdateRoleRequest;
import dto.response.ApiResponse;
import dto.response.user.UserResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping(value = "/v1/create-user", consumes = "application/json", produces = "application/json")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse userResponse = userService.createUser(request);

        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }

    @PostMapping(value = "/update-my-inf",
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"}
    )
    public ApiResponse<UserResponse> updateMyInf(@RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request))
                .build();
    }

    @PostMapping(value = "/update-roles",
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"}
    )
    public ApiResponse<UserResponse> updateRoles(@RequestBody @Valid UserUpdateRoleRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateRoles(request))
                .build();
    }

    @PostMapping(value = "/change-pass",
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"}
    )
    public ApiResponse<UserResponse> changePassword(@RequestBody @Valid UserChangePasswordRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.changePassword(request))
                .build();
    }

    @GetMapping(value = "/v1/{userId}/get-user")
    public ApiResponse<UserResponse> getUser(@PathVariable String userId) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userId))
                .build();
    }

    @GetMapping(value = "/v1/users")
    public ApiResponse<List<UserResponse>> getUsers() {

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping(value = "/v1/user-name")
    public ApiResponse<UserResponse> getUserByUserName() {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByUsername(contextHolder.getAuthentication().getName()))
                .build();
    }

    @GetMapping(value = "/v1/my-info")
    public ApiResponse<UserResponse> getMyInfo() {
        UserResponse userResponse  = userService.getMyInfo();

        return ApiResponse.<UserResponse>builder()
                .result(userResponse)
                .build();
    }
}
