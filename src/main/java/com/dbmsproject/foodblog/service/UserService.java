package com.dbmsproject.foodblog.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dbmsproject.foodblog.entity.User;

public interface UserService extends UserDetailsService {
    
    public User findByUserName(String username);

    public void save(User user);
}
