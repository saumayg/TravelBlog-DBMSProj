package com.dbmsproject.travelblog.dao;

import com.dbmsproject.travelblog.entity.User;

public interface UserDAO {

    public User findByUserName(String userName);

    public void save(User user);
}
