package com.dbmsproject.foodblog.service.impl;

import java.util.List;

import com.dbmsproject.foodblog.dao.PostDAO;
import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

	private PostDAO postDAO;

	@Autowired
	public PostServiceImpl(PostDAO postDAO) {
		this.postDAO = postDAO;
	}

	@Override
	public List<Post> getAll() {

		return postDAO.findAll();
	}
}
