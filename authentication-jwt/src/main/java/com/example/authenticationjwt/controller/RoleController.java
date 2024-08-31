package com.example.authenticationjwt.controller;

import com.example.authenticationjwt.service.RoleService;
import dto.request.role.RoleCreateRequest;
import dto.response.ApiResponse;
import dto.response.RoleResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping(
            value = "/create",
            consumes = {"application/json", "application/x-www-form-urlencoded"},
            produces = {"application/json", "application/xml"}
    )
    public ApiResponse<RoleResponse> createRole(@RequestBody @Valid RoleCreateRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping(
            value = "/get-all",
            produces = {"application/json", "application/xml"}
    )
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }
}
