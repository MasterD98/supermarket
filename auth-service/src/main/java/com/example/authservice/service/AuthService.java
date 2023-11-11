package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try{
            repository.save(user);
            return "user registered";
        }catch (Exception ex) {
            return "user register failed";
        }
    }

    public String generateToken(String username) {
        String token=jwtService.generateToken(username);
        return token;
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

}
