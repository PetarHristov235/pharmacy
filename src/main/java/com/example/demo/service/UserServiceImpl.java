package com.example.demo.service;

import com.example.demo.db.entity.UserEntity;
import com.example.demo.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void banUser(Long id) {
        userRepository.findById(id).ifPresent(user -> user.setActive(false));
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        userRepository.findById(id).ifPresent(user -> user.setActive(true));
    }

    @Override
    public boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
