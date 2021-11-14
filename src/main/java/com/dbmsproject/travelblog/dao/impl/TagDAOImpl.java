package com.dbmsproject.travelblog.dao.impl;

import java.util.List;
import java.util.logging.Logger;

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

	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	public TagDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Get all tags
	@Override
	public List<Tag> findAll() {
		logger.info("TagDAO: findAll()");
		
		Query query = entityManager.createQuery("select t from Tag t");
		List<Tag> tags = AppUtils.castList(Tag.class, query.getResultList());

		return tags;
	}

	///Get tag according to its id (Parameter: int id)
	@Override
	public Tag findById(int id) {
		logger.info("TagDAO: findById(int id)");
		
		Tag tag = entityManager.find(Tag.class, id);

		return tag;
	}

	@Override
	public void saveOrUpdate(Tag tag) {

		Tag dbTag = entityManager.merge(tag);
		tag.setId(dbTag.getId());
	}

	@Override
    public void deleteById(int id) {
		
		Query query = entityManager.createQuery("delete from Tag where id=:tagId");
		query.setParameter("tagId", id);

		query.executeUpdate();
	}
}
