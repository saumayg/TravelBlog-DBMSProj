package com.dbmsproject.travelblog.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.utils.AppUtils;

///Implementation of User DAO Interface
@Repository
public class UserDAOImpl implements UserDAO {

	private EntityManager entityManager;

	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	public UserDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Get all users
	@Override
	public List<User> findAll() {
		logger.info("UserDAO: findAll()");
		
		Query query = entityManager.createQuery("from User p");
		List<User> users = AppUtils.castList(User.class, query.getResultList());
		
		return users;
	}

	///Get a user by its username (Parameter: String username)
	@Override
	public User findByUserName(String userName) {
		logger.info("UserDAO: findByUserName(String userName)");
		
		Query theQuery = entityManager.createQuery("from User where username=:uName", User.class);
		theQuery.setParameter("uName", userName);

		User user = null;
		try {
			user = (User) theQuery.getSingleResult();
		} catch(Exception e) {
			user = null;
		}
		return user;
	}

	///Save a user
	@Override
	public void save(User user) {
		logger.info("UserDAO: save(User user)");

		User dbUser = entityManager.merge(user);
		user.setId(dbUser.getId());
	}

	///Delete a user
	@Override
	public void deleteByUsername(String username) {
		logger.info("UserDAO: deleteByUsername(String username)");

		Query query = entityManager.createQuery("delete from User where username=:uName");
		query.setParameter("uName", username);

		query.executeUpdate();
	}
}
