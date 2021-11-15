package com.dbmsproject.travelblog.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Photo;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.AlbumService;
import com.dbmsproject.travelblog.utils.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

///Implementation for Album Service interface
@Service
public class AlbumServiceImpl implements AlbumService {
    
    private AlbumDAO albumDAO;
    private UserDAO userDAO;
    private PhotoDAO photoDAO;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public AlbumServiceImpl(
        AlbumDAO albumDAO,
        UserDAO userDAO,
        PhotoDAO photoDAO
    ) {
        this.albumDAO = albumDAO;
        this.userDAO = userDAO;
        this.photoDAO = photoDAO;
    }

    ///Get all albums by all users
    @Override
    @Transactional
    public List<Album> getAll() {
        logger.info("AlbumService: getAll()");

        return albumDAO.findAll();
    }

    ///Get 3 latest albums among all users
    @Override
    @Transactional
    public List<Album> getLatestAlbums() {
        logger.info("AlbumService: getLatestAlbums()");

        return albumDAO.findLatestAlbum();
    }

    /// Get a single album using its id (Parameter: Int id)
    @Override
    @Transactional
    public Album getAlbumById(int id) {
        logger.info("AlbumService: getAlbumById(int id)");

        Album albumById = albumDAO.findById(id);
        //If album not found throws exception
        if (albumById == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }

        return albumById;
    }

    ///Save or update album
    @Override
    @Transactional
    public void saveOrUpdate(
        Album newAlbum, 
        Principal principal, 
        boolean update, 
        MultipartFile[] multipartFiles
    ) throws IOException {
        logger.info("AlbumService: saveOrUpdate(Album newAlbum, Principal principal, boolean update, MultipartFile[] multipartFiles)");

        if (!update) {
            //Update is false, save a new album

            //Get new album
            Album album = new Album();
            //Get user
            User user = userDAO.findByUserName(principal.getName());
            
            album = newAlbum;
            //Set user
            album.setUser(user);
            //Set created at
            album.setCreatedAt(Instant.now().plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES));
            //Save album
            albumDAO.saveOrUpdate(album);

            //Save the images input by user and connect them to album
			for (int i = 0 ; i < multipartFiles.length; i++) {
                if (multipartFiles[i].isEmpty())
                    continue;
				Photo photo = new Photo();
				String fileName = StringUtils.cleanPath(multipartFiles[i].getOriginalFilename());
				photo.setName(fileName);
				photo.setAlbum(album);
				photoDAO.save(photo);

				String uploadDir = "images/user" + user.getId() + "/album" + album.getId() + "/" + photo.getId();
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFiles[i]);
			}
        }
        else {
            //Update album details
            Album album = albumDAO.findById(newAlbum.getId());

            if (album == null) {
                throw new ResponseStatusException (
                    HttpStatus.NOT_FOUND, ""
                );
            }

            album.setName(newAlbum.getName());
            album.setDescription(newAlbum.getDescription());

            albumDAO.saveOrUpdate(album);
        }
    }

    ///Delete album
    @Override
    @Transactional
    public void deleteById(int id) throws IOException {
        logger.info("AlbumService: deleteById(int id)");

        Album album = albumDAO.findById(id);

        if (album == null) {
            throw new ResponseStatusException (
                HttpStatus.NOT_FOUND, ""
            );
        }

        //Delete album directory
        String deleteDir = "images/user" + album.getUser().getId() + "/album" + id;
        FileUploadUtil.deleteFile(deleteDir);

        //Delete album (Cascade set in sql script)
        albumDAO.deleteById(id);
    }
}
