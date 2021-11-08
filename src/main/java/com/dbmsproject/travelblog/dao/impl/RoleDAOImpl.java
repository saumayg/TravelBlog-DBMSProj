package com.dbmsproject.travelblog.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dbmsproject.travelblog.dao.RoleDAO;
import com.dbmsproject.travelblog.entity.Role;

@Repository
public class RoleDAOImpl implements RoleDAO {

	private EntityManager entityManager;
	
	@Autowired
	public RoleDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Role findRoleByName(String roleName) {

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
