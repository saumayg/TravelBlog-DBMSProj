package com.dbmsproject.travelblog.service.impl;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.dbmsproject.travelblog.dao.CommentDAO;
import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.Comment;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

///Implementation for Comment service
@Service
public class CommentServiceImpl implements CommentService {

    private CommentDAO commentDAO;
    private PostDAO postDAO;
    private UserDAO userDAO;

    @Autowired
    public CommentServiceImpl(
        CommentDAO commentDAO,
        PostDAO postDAO,
        UserDAO userDAO
    ) {
        this.commentDAO = commentDAO;
        this.postDAO = postDAO;
        this.userDAO = userDAO;
    }

    ///Save or update a comment
    @Override
    @Transactional
    public void saveOrUpdate(Comment newComment, Principal principal, int postId) {

        //Find and set post info
        Post post = postDAO.findById(postId);
        if (post == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }
        newComment.setPost(post);

        //Find and set user info
        User user = userDAO.findByUserName(principal.getName());
        newComment.setUser(user);

        //Set created at
        newComment.setCreatedAt(Instant.now().plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES));

        commentDAO.saveOrUpdate(newComment);
    }

    @Override
    @Transactional
    public void delete(int id) {
        commentDAO.delete(id);
    }
}
