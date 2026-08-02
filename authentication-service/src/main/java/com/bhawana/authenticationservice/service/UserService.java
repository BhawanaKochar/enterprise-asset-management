package com.bhawana.authenticationservice.service;

import com.bhawana.authenticationservice.dto.RegisterRequest;
import com.bhawana.authenticationservice.entity.User;

import java.util.List;

public interface UserService {

    User register(RegisterRequest request);

    List<User> getAllUsers();

    User getUserById(Long id);

    void deleteUser(Long id);
}