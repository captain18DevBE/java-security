package com.example.authenticationjwt.configuration;

import com.example.authenticationjwt.entity.Permission;
import com.example.authenticationjwt.entity.Role;
import com.example.authenticationjwt.entity.User;
import com.example.authenticationjwt.repository.PermissionRepository;
import com.example.authenticationjwt.repository.RoleRepository;
import com.example.authenticationjwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


@Configuration
@Slf4j
public class ApplicationInitConfig {


    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    )
    {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {

                Permission permission = new Permission("UPDATE_ROLES", "This scope to update role for user account");
                permissionRepository.save(permission);
                Set<Permission> permissions = new HashSet<>();
                permissions.add(permission);

                Role role = new Role("ADMIN", "This is admin account",permissions);
                roleRepository.save(role);
                Set<Role> roles = new HashSet<>();
                roles.add(role);

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository .save(user);
                log.warn("Admin user created, Pls change password!");
            }

        };
    }
}
