package com.dbmsproject.travelblog.dao;

import java.util.List;

import com.dbmsproject.travelblog.entity.Post;

///Post DAO interface
public interface PostDAO {
    
    /// Get all posts by all users
    public List<Post> findAll();

    /// Get all posts by a single user (Parameter: String username)
    public List<Post> findByUsername(String username); 

    /// Get 3 posts among all users randomly 
    public List<Post> findRandomPost();
    
    /// Get 3 latest posts among all users 
    public List<Post> findLatestPost();

    /// Get a single post using its id (Parameter: Int id)
    public Post findById(int id);

    // Get all posts under a particular tag (Parameter: int id)
    public List<Post> findByTag(int id);

    // delete
    public void deleteById(int id);

    // add / update
    public void saveOrUpdate(Post post);
}
