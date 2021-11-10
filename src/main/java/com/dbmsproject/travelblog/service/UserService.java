package com.dbmsproject.travelblog.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import com.dbmsproject.travelblog.entity.User;

///User service interface
public interface UserService extends UserDetailsService {

    ///Get all users
    public List<User> findAll();
    
    ///Get a user by its username (Parameter: String username)
    public User findByUserName(String username);

    ///Save a user
    public void save(User user);
}
