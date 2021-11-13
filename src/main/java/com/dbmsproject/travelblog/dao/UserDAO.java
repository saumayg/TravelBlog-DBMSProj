package com.dbmsproject.travelblog.dao;

import java.util.List;

import com.dbmsproject.travelblog.entity.User;

///User DAO Interface
public interface UserDAO {

    ///Get all users
    public List<User> findAll();

    ///Get a user by its username (Parameter: String username)
    public User findByUserName(String username);

    ///Save a user
    public void save(User user);

    ///Delete a user
    public void deleteByUsername(String username);
}
