package com.example.authenticationjwt.mapper;

import com.example.authenticationjwt.entity.Permission;
import dto.request.permission.PermissionRequest;
import dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
}
