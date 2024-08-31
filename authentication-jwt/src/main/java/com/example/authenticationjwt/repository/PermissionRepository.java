package com.example.authenticationjwt.repository;

import com.example.authenticationjwt.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    @Query("select (count(p) > 0) from Permission p where p.name = ?1")
    boolean existsByName(String name);

    Optional<Permission> findByName(String name);
}
