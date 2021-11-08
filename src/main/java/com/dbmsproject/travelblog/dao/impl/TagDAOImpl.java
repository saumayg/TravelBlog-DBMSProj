package com.dbmsproject.travelblog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbmsproject.travelblog.dao.TagDAO;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.utils.AppUtils;

///Implementation of Tag DAO Interface
@Repository
public class TagDAOImpl implements TagDAO {

	private EntityManager entityManager;

	@Autowired
	public TagDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Get all tags
	@Override
	public List<Tag> findAll() {
		
		Query query = entityManager.createQuery("select t from Tag t");
		List<Tag> tag = AppUtils.castList(Tag.class, query.getResultList());

		return tag;
	}

	///Get tag according to its id (Parameter: int id)
	@Override
	public Tag findById(int id) {
		
		Tag tag = entityManager.find(Tag.class, id);

		return tag;
	}

}
