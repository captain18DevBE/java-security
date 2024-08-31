package com.example.authenticationjwt.repository;

import com.example.authenticationjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    @Modifying
    @Query("""
        update User u set u.password = ?1 where u.username = ?2
    """)
    User updatePassword(String username, String password);
}
