package com.dbmsproject.foodblog.dao;

import com.dbmsproject.foodblog.entity.User;

public interface UserDAO {

    public User findByUserName(String userName);

    public void save(User user);
}
