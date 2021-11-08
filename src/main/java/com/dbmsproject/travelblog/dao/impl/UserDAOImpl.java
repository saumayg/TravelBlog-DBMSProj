package com.dbmsproject.foodblog.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbmsproject.foodblog.dao.UserDAO;
import com.dbmsproject.foodblog.entity.User;

@Repository
public class UserDAOImpl implements UserDAO {

	private EntityManager entityManager;

	@Autowired
	public UserDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public User findByUserName(String userName) {
		
		Query theQuery = entityManager.createQuery("from User where userName=:uName", User.class);
		theQuery.setParameter("uName", userName);

		User user = null;
		try {
			user = (User) theQuery.getSingleResult();
		} catch(Exception e) {
			user = null;
		}
		return user;
	}

	@Override
	public void save(User user) {
		entityManager.merge(user);
	}

}
