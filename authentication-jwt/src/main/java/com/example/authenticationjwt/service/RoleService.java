package com.example.authenticationjwt.service;

import com.example.authenticationjwt.entity.Role;
import com.example.authenticationjwt.mapper.RoleMapper;
import com.example.authenticationjwt.repository.PermissionRepository;
import com.example.authenticationjwt.repository.RoleRepository;
import dto.request.role.RoleCreateRequest;
import dto.response.RoleResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Service
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleCreateRequest request) {
        Role role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(
                permissionRepository.findAllById(request.getPermissions()))
        );
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

}
