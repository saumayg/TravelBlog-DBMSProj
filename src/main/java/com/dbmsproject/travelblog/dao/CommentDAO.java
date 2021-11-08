package com.dbmsproject.travelblog.dao;

import java.util.List;

import com.dbmsproject.travelblog.entity.Comment;

///Comment DAO Interface
public interface CommentDAO {

    ///Get all comments by all users
    public List<Comment> findAll();

    ///Get all comments under a post
    public List<Comment> findByPostId(int id);

    ///Save or Update a comment
    public void saveOrUpdate(Comment comment);
}
