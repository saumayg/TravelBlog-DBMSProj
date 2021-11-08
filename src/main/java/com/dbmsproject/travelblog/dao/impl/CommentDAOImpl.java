package com.dbmsproject.travelblog.dao.impl;

import java.util.List;

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

	@Autowired
	public CommentDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Get all comments by all users
	@Override
	public List<Comment> findAll() {
		
		Query query = entityManager.createQuery("select c from Comment c order by c.createdAt desc");
		List<Comment> comment = AppUtils.castList(Comment.class, query.getResultList());

		return comment;
	}

    ///Get all comments under a post
	@Override
	public List<Comment> findByPostId(int id) {

		Query query = entityManager.createQuery("select c from Comment c where c.post.id=:postId order by asc");
		query.setParameter("postId", id);

		List<Comment> comment = AppUtils.castList(Comment.class, query.getResultList());

		return comment;
	}

	@Override
	public void saveOrUpdate(Comment comment) {

		System.out.println("Saving/Updating comment with id = " + comment.getId());

		Comment dbComment = entityManager.merge(comment);
		comment.setId(dbComment.getId());
	}
}
