package com.dbmsproject.foodblog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dbmsproject.foodblog.dao.PostDAO;
import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

///Implementation of Post DAO interface
@Repository
public class PostDAOImpl implements PostDAO {

	private EntityManager entityManager;

	@Autowired
	public PostDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	/// Get all posts by all users
	public List<Post> findAll() {
		
		Query query = entityManager.createQuery("select p from Post p order by p.createdAt desc");
		List<Post> post = AppUtils.castList(Post.class, query.getResultList());
		
		return post;
	}

	@Override
    /// Get all posts by a single user (Parameter: Int id)
	public List<Post> findByUserId(int id) {
		
		Query query = entityManager.createQuery("SELECT p FROM Post p where p.user.id=:userId");
		query.setParameter("userId", id);

		List<Post> post = AppUtils.castList(Post.class, query.getResultList());

		return post;
	}

	///Get 3 posts among all users randomly 
	@Override
	public List<Post> findRandomPost() {
		
		Query query = entityManager.createQuery("select p from Post as p order by rand()");
		query.setMaxResults(3);
		List<Post> post = AppUtils.castList(Post.class, query.getResultList());

		return post;
	}
	
	///Get 3 latest posts among all users 
	@Override
	public List<Post> findLatestPost() {
		
		Query query = entityManager.createQuery("select p from Post as p order by p.createdAt desc");
		query.setMaxResults(3);
		List<Post> post = AppUtils.castList(Post.class, query.getResultList());

		return post;
	}

	/// Get a single post using its id (Parameter: Int id)
	@Override
	public Post findById(int id) {
		
		Post post = entityManager.find(Post.class, id);

		return post;
	}

	///Get all posts under a particular tag (Parameter: int id)
	@Override
	public List<Post> findByTag(int id) {
		
		Query query = entityManager.createQuery("select p from Post p join p.tag t where t.id=:tagId");
		query.setParameter("tagId", id);
		List<Post> post = AppUtils.castList(Post.class, query.getResultList());

		return post;
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
}
