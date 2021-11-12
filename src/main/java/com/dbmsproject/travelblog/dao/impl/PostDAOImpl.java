package com.dbmsproject.travelblog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.utils.AppUtils;

///Implementation of Post DAO interface
@Repository
public class PostDAOImpl implements PostDAO {

	private EntityManager entityManager;

	@Autowired
	public PostDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/// Get all posts by all users
	@Override
	public List<Post> findAll() {
		
		Query query = entityManager.createQuery("select p from Post p order by p.createdAt desc");
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

	///Get all posts by the user in sorted fashion(Parameter: String username)
	public List<Post> allPostsSortedByUser(String username) {

		Query query = entityManager.createQuery("select p from Post p where p.user.username=:uName order by p.createdAt desc");
		query.setParameter("uName", username);

		List<Post> posts = AppUtils.castList(Post.class, query.getResultList());

		return posts;
	}

	///Get latest 3 posts by the user in sorted fashion(Parameter: String username)
	public List<Post> allLatestPostsSortedByUser(String username) {

		Query query = entityManager.createQuery("select p from Post p where p.user.username=:uName order by p.createdAt desc");
		query.setMaxResults(3);
		query.setParameter("uName", username);

		List<Post> posts = AppUtils.castList(Post.class, query.getResultList());

		return posts;
	}

	///Get all posts by the tag in sorted fashion(Parameter: int id)
	public List<Post> allPostsSortedByTag(int id) {

		Query query = entityManager.createQuery("select p from Post p join p.tags t where t.id=:idTag order by p.createdAt desc");
		query.setParameter("idTag", id);

		List<Post> posts = AppUtils.castList(Post.class, query.getResultList());

		return posts;
	}

	@Override
	public void deleteById(int id) {
		
		Query query = entityManager.createQuery("delete from Post where id=:postId");
		query.setParameter("postId", id);

		query.executeUpdate();
	}

	// add / update
	@Override
	public void saveOrUpdate(Post post) {

		System.out.println(post.getId());
		
		Post dbPost = entityManager.merge(post);
		post.setId(dbPost.getId());
	}
}
