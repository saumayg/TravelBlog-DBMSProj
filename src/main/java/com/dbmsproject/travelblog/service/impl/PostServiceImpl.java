package com.dbmsproject.travelblog.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.dao.TagDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Photo;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.utils.FileUploadUtil;

///Implementation for Post service
@Service
public class PostServiceImpl implements PostService {

	private PostDAO postDAO;
	private UserDAO userDAO;
	private TagDAO tagDAO;
	private AlbumDAO albumDAO;
	private PhotoDAO photoDAO;

	@Autowired
	public PostServiceImpl(
		PostDAO postDAO,
		UserDAO userDAO,
		TagDAO tagDAO,
		AlbumDAO albumDAO,
		PhotoDAO photoDAO
	) {
		this.postDAO = postDAO;
		this.userDAO = userDAO;
		this.tagDAO = tagDAO;
		this.albumDAO = albumDAO;
		this.photoDAO = photoDAO;
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
	public void saveOrUpdate(Post newPost, Principal principal, int[] tagList, boolean update, MultipartFile[] multipartFiles) throws IOException {

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

			//Assigns album details to empty album, saves it and then joins to post enity
			Album album = new Album();
			album = newPost.getAlbum();
			albumDAO.saveOrUpdate(album);
			newPost.setAlbum(album);

			//Save post
			postDAO.saveOrUpdate(newPost);

			//Save the images input by user and connect them to album
			for (int i = 0 ; i < multipartFiles.length; i++) {
				Photo photo = new Photo();
				String fileName = StringUtils.cleanPath(multipartFiles[i].getOriginalFilename());
				photo.setName(fileName);
				photo.setAlbum(newPost.getAlbum());
				photoDAO.save(photo);

				String uploadDir = "images/album" + album.getId() + "/" + photo.getId();
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFiles[i]);
			}
		}
		else {
			//Find old post data
			Post post = postDAO.findById(newPost.getId());
			Album album = post.getAlbum();
			Album newAlbum = newPost.getAlbum();

			if (post == null || album == null) {
				throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, ""
				);
			}

			//Update post data
			post.setTitle(newPost.getTitle());
			post.setBody(newPost.getBody());
			post.setDescription(newPost.getDescription());
			post.setTags(newPost.getTags());
			
			//Update album data
			album.setName(newAlbum.getName());
			album.setDescription(newAlbum.getDescription());

			//Save updated album data
			albumDAO.saveOrUpdate(album);
			//Save updated post data
			postDAO.saveOrUpdate(post);
		}

		
	}
}
