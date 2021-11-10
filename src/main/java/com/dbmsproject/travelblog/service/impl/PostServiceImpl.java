package com.dbmsproject.travelblog.service.impl;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.dao.TagDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.PostService;

///Implementation for Post service
@Service
public class PostServiceImpl implements PostService {

	private PostDAO postDAO;
	private UserDAO userDAO;
	private TagDAO tagDAO;

	@Autowired
	public PostServiceImpl(
		PostDAO postDAO,
		UserDAO userDAO,
		TagDAO tagDAO
	) {
		this.postDAO = postDAO;
		this.userDAO = userDAO;
		this.tagDAO = tagDAO;
	}

	/// Get all posts by all users
	@Override
	@Transactional
	public List<Post> getAll() {

		return postDAO.findAll();
	}
	
	///Get 3 posts among all users randomly
	@Override
	@Transactional
    public List<Post> getRandomPost() {
    	
    	return postDAO.findRandomPost();
    };

    ///Get 3 latest posts among all users
	@Override
	@Transactional
	public List<Post> getLatestPost() {
		
		return postDAO.findLatestPost();
	}

    /// Get a single post using its id (Parameter: Int id)
	@Override
	@Transactional
	public Post getPostById(int id) {

		Post postById = postDAO.findById(id);
		//If post not found throws exception
		if (postById == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Page not found"
            );
        }

		return postById;
	}

	///Save or update a new post
	@Override
	@Transactional
	public void saveOrUpdate(Post newPost, Principal principal, int[] tagList, boolean update) {

		//Sets incoming tags
		newPost.setTags(new ArrayList<>());
		if (tagList != null) {
            Tag tag = null;
            for (int i = 0 ; i < tagList.length; i++) {
                tag = new Tag();
                tag = tagDAO.findById(tagList[i]);
                newPost.getTags().add(tag);
            }
        }
		
		if (!update) {

			//Find and set user info
			User user = userDAO.findByUserName(principal.getName());
			newPost.setUser(user);
			//Set created time
			newPost.setCreatedAt(Instant.now().plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES));

			//Save post
			postDAO.saveOrUpdate(newPost);
		}
		else {
			//Find old post data
			Post post = postDAO.findById(newPost.getId());

			if (post == null) {
				throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ""
				);
			}

			//Update data
			post.setTitle(newPost.getTitle());
			post.setBody(newPost.getBody());
			post.setDescription(newPost.getDescription());
			post.setTags(newPost.getTags());

			//Save updated data
			postDAO.saveOrUpdate(post);
		}

		
	}
}
