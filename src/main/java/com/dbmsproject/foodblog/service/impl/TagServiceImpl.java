package com.dbmsproject.foodblog.service.impl;

import java.util.List;

import com.dbmsproject.foodblog.dao.TagDAO;
import com.dbmsproject.foodblog.entity.Tag;
import com.dbmsproject.foodblog.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

///Implementation for tag service
@Service
public class TagServiceImpl implements TagService {

	private TagDAO tagDAO;

	@Autowired
	public TagServiceImpl(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	///Get all tags
	@Override
	public List<Tag> getAll() {
		
		return tagDAO.findAll();
	}

	///Get tag according to its id (Parameter: int id)
	@Override
	public Tag getTagById(int id) {
		
		return tagDAO.findById(id);
	}

}
