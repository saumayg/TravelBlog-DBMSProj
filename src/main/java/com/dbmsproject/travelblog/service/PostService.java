package com.dbmsproject.travelblog.service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.dbmsproject.travelblog.entity.Post;

import org.springframework.web.multipart.MultipartFile;

///Post service interface
public interface PostService {
	
	/// Get all posts by all users
    public List<Post> getAll();

    ///Get 3 posts among all users randomly
    public List<Post> getRandomPosts();
    
    ///Get 3 latest posts among all users
    public List<Post> getLatestPosts();

    /// Get a single post using its id (Parameter: Int id)
    public Post getPostById(int id);

    ///Save a new post or update a post
    public void saveOrUpdate(Post post, Principal principal, int[] tagList, String albumName, String albumDescription, boolean update, MultipartFile[] multipartFiles) throws IOException;

    ///Delete a post
    public void deleteById(int id) throws IOException;
}
