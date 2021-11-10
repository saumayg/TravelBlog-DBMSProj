package com.dbmsproject.travelblog.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbmsproject.travelblog.dao.RoleDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.Role;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.UserService;

///Implementation for user service
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public List<User> findAll() {
		
		return userDAO.findAll();
	}

	///Get all users
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}
		return new org.springframework.security.core.userdetails.User(
			user.getUsername(),
			user.getPassword(),
			mapRolesToAuthorities(user.getRoles())
		);
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	///Get a user by its username (Parameter: String username)
	@Override
	@Transactional
	public User findByUserName(String username) {
		return userDAO.findByUserName(username);
	}

	///Save a user
	@Override
	@Transactional
	public void save(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(roleDAO.findRoleByName("ROLE_USER")));
		userDAO.save(user);
	}
}
