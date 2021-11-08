package com.dbmsproject.travelblog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.service.PostService;

///Implementation for Post service
@Service
public class PostServiceImpl implements PostService {

	private PostDAO postDAO;

	@Autowired
	public PostServiceImpl(PostDAO postDAO) {
		this.postDAO = postDAO;
	}

	/// Get all posts by all users
	@Override
	public List<Post> getAll() {

		return postDAO.findAll();
	}

	/// Get all posts by a single user (Parameter: String username)
	@Override
	public List<Post> getPostByUsername(String username) {
		
		return postDAO.findByUsername(username);
	}
	
	///Get 3 posts among all users randomly
    public List<Post> getRandomPost() {
    	
    	return postDAO.findRandomPost();
    };

    ///Get 3 latest posts among all users
	@Override
	public List<Post> getLatestPost() {
		
		return postDAO.findLatestPost();
	}

    /// Get a single post using its id (Parameter: Int id)
	@Override
	public Post getPostById(int id) {

		return postDAO.findById(id);
	}

	// Get all posts under a particular tag (Parameter: int id)
    public List<Post> getPostByTag(int id) {

		return postDAO.findByTag(id);
	}
}
