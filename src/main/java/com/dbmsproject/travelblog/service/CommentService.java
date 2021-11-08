package com.dbmsproject.travelblog.service;

import java.security.Principal;

import com.dbmsproject.travelblog.entity.Comment;

public interface CommentService {

    ///Save or update a comment
    public void saveOrUpdate(Comment comment, Principal principal, int postId);
}
