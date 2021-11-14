package com.dbmsproject.travelblog.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dbmsproject.travelblog.dao.CommentDAO;
import com.dbmsproject.travelblog.entity.Comment;
import com.dbmsproject.travelblog.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

///Implementation of CommentDAO Interface
@Repository
public class CommentDAOImpl implements CommentDAO {

	private EntityManager entityManager;

	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	public CommentDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Get all comments by all users
	@Override
	public List<Comment> findAll() {
		logger.info("CommentDAO: findAll()");
		
		Query query = entityManager.createQuery("select c from Comment c order by c.createdAt desc");
		List<Comment> comments = AppUtils.castList(Comment.class, query.getResultList());

		return comments;
	}

	///Get comment by id
	public Comment findById(int id) {
		logger.info("CommentDAO: findById(int id)");

		Comment comment = entityManager.find(Comment.class, id);

		return comment;
	}

	///Save or update a comment
	@Override
	public void saveOrUpdate(Comment comment) {
		logger.info("CommentDAO: saveOrUpdate(Comment comment)");

		Comment dbComment = entityManager.merge(comment);
		comment.setId(dbComment.getId());
	}

	///Delete a comment
	@Override
	public void deleteById(int id) {
		logger.info("CommentDAO: deleteById(int id)");
		
		Query query = entityManager.createQuery("delete from Comment where id=:cmntId");
		query.setParameter("cmntId", id);

		query.executeUpdate();
	}
}
