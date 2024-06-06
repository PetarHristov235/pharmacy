package com.example.demo.db.repository;

import com.example.demo.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("""
            SELECT EXISTS(SELECT 1 FROM UserEntity u WHERE u.username = :username AND u.password = :password)""")
    Boolean existByUsernameAndPassword(String username, String password);

    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
