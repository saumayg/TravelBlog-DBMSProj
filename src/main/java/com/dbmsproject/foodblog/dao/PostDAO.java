package com.dbmsproject.foodblog.dao;

import java.util.List;

import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.entity.Tag;

///Post DAO interface
public interface PostDAO {
    
    /// Get all posts by all users
    public List<Post> findAll();

    /// Get all posts by a single user (Parameter: Int id)
    public List<Post> findByUserId(int id); 

    /// Get 3 posts among all users randomly 
    public List<Post> findRandomPost();
    
    /// Get 3 latest posts among all users 
    public List<Post> findLatestPost();

    // tag
    public List<Post> findByTag(List<Tag> tag);

    // delete
    public void deleteById(int id);

    // add / update
    public void saveOrUpdate(Post post);

    // post (id)
    public Post findById(int id);
}
