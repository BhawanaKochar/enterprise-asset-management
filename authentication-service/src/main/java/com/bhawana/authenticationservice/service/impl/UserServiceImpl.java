package com.bhawana.authenticationservice.service.impl;

import com.bhawana.authenticationservice.dto.RegisterRequest;
import com.bhawana.authenticationservice.entity.User;
import com.bhawana.authenticationservice.repository.UserRepository;
import com.bhawana.authenticationservice.service.UserService;
import com.bhawana.commonlibrary.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User register(RegisterRequest request) {

        repository.findByUsername(request.getUsername())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Username already exists.");
                });

        repository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email already exists.");
                });

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())      // Will use BCrypt later
                .email(request.getEmail())
                .role(request.getRole())
                .enabled(true)
                .build();

        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id : " + id));
    }

    @Override
    public void deleteUser(Long id) {

        User user = getUserById(id);

        repository.delete(user);
    }
}