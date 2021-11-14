package com.dbmsproject.travelblog.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.dao.TagDAO;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.TagService;

///Implementation for tag service
@Service
public class TagServiceImpl implements TagService {

	private TagDAO tagDAO;
	private PostDAO postDAO;

	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	public TagServiceImpl(
		TagDAO tagDAO,
		PostDAO postDAO
	) {
		this.tagDAO = tagDAO;
		this.postDAO = postDAO;
	}

	///Get all tags
	@Override
	@Transactional
	public List<Tag> getAll() {
		logger.info("TagService: findAll()");
		
		return tagDAO.findAll();
	}

	///Get all posts by the tag in sorted fashion(Parameter: int id)
	@Override
	@Transactional
    public List<Post> getAllPostsSorted(int id) {
		logger.info("TagService: getAllPostsSorted(int id)");

		//Get tag informmation
		Tag tag = tagDAO.findById(id);

		//Throws exception if tag not found
		if (tag == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, ""
			);
		}

		return postDAO.allPostsSortedByTag(id);
	}

	///Get tag according to its id (Parameter: int id)
	@Override
	@Transactional
	public Tag getTagById(int id) {
		logger.info("TagService: getTagById(int id)");
		
		//Get tag informmation
		Tag tag = tagDAO.findById(id);

		//Throws exception if tag not found
		if (tag == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, ""
			);
		}

		return tag;
	}

	@Override
	@Transactional
	public void save(Tag newTag) {
		
		newTag.setCreatedAt(Instant.now().plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES));
		tagDAO.saveOrUpdate(newTag);
	}

	@Override
	@Transactional
	public void deleteById(int id) {

		Tag tag = tagDAO.findById(id);

		if (tag == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, ""
			);
		}

		tagDAO.deleteById(id);
	}
}
