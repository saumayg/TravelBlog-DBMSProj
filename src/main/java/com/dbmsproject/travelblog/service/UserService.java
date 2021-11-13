package com.dbmsproject.travelblog.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.User;

///User service interface
public interface UserService extends UserDetailsService {

    ///Get all users
    public List<User> findAll();
    
    ///Get a user by its username (Parameter: String username)
    public User findByUserName(String username);

    ///Get all posts by the user in sorted fashion(Parameter: String username)
    public List<Post> getAllPostsSorted(String username);

    ///Get latest 3 posts by the user in sorted fashion(Parameter: String username)
    public List<Post> getAllLatestPostsSorted(String username);

    ///Get all albums by the user in sorted fashion(Parameter: String username)
    public List<Album> getAllAlbumsSorted(String username);

    ///Get latest 3 albums by the user in sorted fashion(Parameter: String username)
    public List<Album> getAllLatestAlbumsSorted(String username);

    ///Update profile photo of the user
    public void updateProfilePhoto(Principal principal, String username, MultipartFile multipartFile) throws IOException;

    ///Save a user
    public void save(User user, boolean update, MultipartFile multipartFile) throws IOException;
}
