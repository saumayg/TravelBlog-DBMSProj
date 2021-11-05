package com.dbmsproject.foodblog.service;

import java.util.List;

import com.dbmsproject.foodblog.entity.Post;

///Post service interface
public interface PostService {
	
	/// Get all posts by all users
    public List<Post> getAll();

    /// Get all posts by a single user (Parameter: Int id)
    public List<Post> getPostByUserId(int id);

    ///Get 3 posts among all users randomly
    public List<Post> getRandomPost();
    
    ///Get 3 latest posts among all users
    public List<Post> getLatestPost();
}
