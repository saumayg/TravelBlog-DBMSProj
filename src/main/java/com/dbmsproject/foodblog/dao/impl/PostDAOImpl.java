package com.dbmsproject.foodblog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dbmsproject.foodblog.dao.PostDAO;
import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.entity.Tag;
import com.dbmsproject.foodblog.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostDAOImpl implements PostDAO {

	private EntityManager entityManager;

	@Autowired
	public PostDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Post> findAll() {
		
		Query query = entityManager.createQuery("from Post");
		List<Post> post = AppUtils.castList(Post.class, query.getResultList());
		
		return post;
	}

	@Override
	public List<Post> findByCreatedBy(int id) {
		
		Query query = entityManager.createQuery("SELECT p FROM Post p where p.user_id=:userId");
		query.setParameter("userId", id);

		List<Post> post = AppUtils.castList(Post.class, query.getResultList());

		return post;
	}

	@Override
	public List<Post> findByTag(List<Tag> tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(int id) {
		
		Query query = entityManager.createQuery("delete from Post where id=:postId");
		query.setParameter("postId", id);

		query.executeUpdate();
	}

	@Override
	public void saveOrUpdate(Post post) {
		
		Post dbPost = entityManager.merge(post);
		post.setId(dbPost.getId());
	}

	@Override
	public Post findById(int id) {
		
		Post post = entityManager.find(Post.class, id);

		return post;
	}

}
