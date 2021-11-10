package com.dbmsproject.travelblog.dao.impl;

import java.util.List;

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

	@Autowired
	public UserDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Get all users
	@Override
	public List<User> findAll() {
		
		Query query = entityManager.createQuery("from User p");
		List<User> users = AppUtils.castList(User.class, query.getResultList());
		
		return users;
	}

	///Get a user by its username (Parameter: String username)
	@Override
	public User findByUserName(String userName) {
		
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
		entityManager.merge(user);
	}

}
