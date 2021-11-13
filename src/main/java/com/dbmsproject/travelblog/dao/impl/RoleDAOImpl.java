package com.dbmsproject.travelblog.dao.impl;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbmsproject.travelblog.dao.RoleDAO;
import com.dbmsproject.travelblog.entity.Role;

///Implementation of Role DAO Interface
@Repository
public class RoleDAOImpl implements RoleDAO {

	private EntityManager entityManager;

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	public RoleDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	///Find role using its name (Parameter: String rolename)
	@Override
	public Role findRoleByName(String roleName) {
		logger.info("RoleDAO: findRoleByName(String roleName)");

		Query theQuery = entityManager.createQuery("from Role where name=:roleName", Role.class);
		theQuery.setParameter("roleName", roleName);

		Role role = null;
		try {
			role = (Role) theQuery.getSingleResult();
		} catch(Exception e) {
			role = null;
		}
		return role;
	}

}
