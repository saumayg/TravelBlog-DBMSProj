package com.dbmsproject.travelblog.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.dao.RoleDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.dao.PostDAO;
import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Photo;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Role;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.UserService;
import com.dbmsproject.travelblog.utils.FileUploadUtil;

///Implementation for user service
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private PostDAO postDAO;

	@Autowired
	private PhotoDAO photoDAO;
	
	@Autowired
	private AlbumDAO albumDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private Logger logger = Logger.getLogger(getClass().getName());

	///Get all users
	@Override
	@Transactional
	public List<User> findAll() {
		logger.info("UserService: findAll()");
		
		return userDAO.findAll();
	}

	///Load user
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
		logger.info("UserService: findByUserName(String username)");

		User user =  userDAO.findByUserName(username);

		if (user == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Page not found"
			);
		}

		return user;
	}

	///Get all posts by the user in sorted fashion(Parameter: String username)
	@Override
	@Transactional
    public List<Post> getAllPostsSorted(String username) {
		logger.info("UserService: getAllPostsSorted(String username)");

		return postDAO.allPostsSortedByUser(username);
	}

	///Get latest 3 posts by the user in sorted fashion(Parameter: String username)
	@Override
	@Transactional
    public List<Post> getAllLatestPostsSorted(String username) {
		logger.info("UserService: getAllLatestPostsSorted(String username)");

		return postDAO.allLatestPostsSortedByUser(username);
	}

	///Get all albums by the user in sorted fashion(Parameter: String username)
	@Override
	@Transactional
    public List<Album> getAllAlbumsSorted(String username) {
		logger.info("UserService: getAllAlbumsSorted(String username)");

		return albumDAO.allAlbumsSortedByUser(username);
	}

	///Get latest 3 albums by the user in sorted fashion(Parameter: String username)
	@Override
	@Transactional
    public List<Album> getAllLatestAlbumsSorted(String username) {
		logger.info("UserService: getAllLatestAlbumsSorted(String username)");

		return albumDAO.allLatestAlbumsSortedByUser(username);
	}

	///Update profile photo of user
	@Override
	@Transactional
	public void updateProfilePhoto(
		Principal principal, 
		String username, 
		MultipartFile multipartFile
	) throws IOException {
		logger.info("UserService: updateProfilePhoto(Principal principal, String username, MultipartFile multipartFile)");

		//Gets the user by username
		User user = userDAO.findByUserName(username);

		if (user == null) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, ""
			);
		}

		//Get user profile photo
		Photo photo = user.getProfilePhoto();

		String uploadDir;

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		//If no user profilPhoto, saves it as new photo
		if (photo == null) {

			photo = new Photo();
			photo.setName(fileName);
			photo.setUser(user);
			photoDAO.save(photo);

			uploadDir = "images/user" + user.getId() + "/" + photo.getId();
		}
		else {
			//First deletes the existing profilePhoto from image directory
			uploadDir = "images/user" + user.getId() + "/" + photo.getId();
			FileUploadUtil.deleteFile(uploadDir);

			photo.setName(fileName);
			photoDAO.save(photo);
		}

		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	}

	///Save a user
	@Override
	@Transactional
	public void save(
		User user, 
		boolean update, 
		MultipartFile multipartFile
	) throws IOException {
		logger.info("UserService: save(User user, boolean update, MultipartFile multipartFile)");

		//If update is false then save
		if (!update) {
			//Save user
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRoles(Arrays.asList(roleDAO.findRoleByName("ROLE_USER")));
			userDAO.save(user);

			//New profilePhoto
			Photo profilePhoto = new Photo();
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			profilePhoto.setName(fileName);
			profilePhoto.setUser(user);
			photoDAO.save(profilePhoto);

			String uploadDir = "images/user" + user.getId() + "/" + profilePhoto.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		else {
			//Update is true
			//Old user details
			User oldUser = userDAO.findByUserName(user.getUsername());

			//Set details
			oldUser.setFirstName(user.getFirstName());
			oldUser.setLastName(user.getLastName());
			oldUser.setEmail(user.getEmail());
			oldUser.setPhone(user.getPhone());

			//Save
			userDAO.save(oldUser);
		}
	}


}
