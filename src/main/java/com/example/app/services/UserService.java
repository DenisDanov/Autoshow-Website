package com.example.app.services;

import com.example.app.data.entities.User;

import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    User save(User user);
}
