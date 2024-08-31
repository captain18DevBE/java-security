package com.example.authenticationjwt.mapper;

import com.example.authenticationjwt.entity.Role;
import dto.request.role.RoleCreateRequest;
import dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreateRequest request);

    RoleResponse toRoleResponse(Role role);
}
