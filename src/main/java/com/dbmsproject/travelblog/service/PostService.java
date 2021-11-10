package com.dbmsproject.travelblog.service;

import java.security.Principal;
import java.util.List;

import com.dbmsproject.travelblog.entity.Post;

///Post service interface
public interface PostService {
	
	/// Get all posts by all users
    public List<Post> getAll();

    ///Get 3 posts among all users randomly
    public List<Post> getRandomPost();
    
    ///Get 3 latest posts among all users
    public List<Post> getLatestPost();

    /// Get a single post using its id (Parameter: Int id)
    public Post getPostById(int id);

    ///Save a new post
    public void saveOrUpdate(Post post, Principal principal, int[] tagList, boolean update);
}
