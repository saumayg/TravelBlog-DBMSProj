package com.dbmsproject.foodblog.service.impl;

import java.util.List;

import com.dbmsproject.foodblog.dao.PostDAO;
import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	/// Get all posts by a single user (Parameter: Int id)
	@Override
	public List<Post> getPostByUserId(int id) {
		
		return postDAO.findByUserId(id);
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
}
