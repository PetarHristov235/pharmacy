package com.example.demo.service;

import com.example.demo.db.entity.UserEntity;
import com.example.demo.db.entity.enums.Role;
import com.example.demo.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(Role.USER);
        user = userRepository.save(user);
        user.setCartNumber(Instant.now().toEpochMilli());
        return user;
    }

    @Override
    public boolean existUserByUsernameAndPassword(String username, String password) {
        return userRepository.existByUsernameAndPassword(username, passwordEncoder.encode(password));
    }
}
