package com.example.demo.service;

import com.example.demo.db.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    UserEntity getUserByUsername(String username);
    List<UserEntity> findAllUsers();
    void deleteUser(Long id);
    void banUser(Long id);
    void activateUser(Long id);
    boolean existsUsername(String username);
    boolean existsUserEmail(String email);
    boolean existUserByUsernameAndPassword(String username, String password);
}
