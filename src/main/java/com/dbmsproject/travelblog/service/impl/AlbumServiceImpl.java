package com.dbmsproject.travelblog.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

@Service
public class AlbumServiceImpl implements AlbumService {
    
    private AlbumDAO albumDAO;
    private UserDAO userDAO;
    private PhotoDAO photoDAO;

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
        return albumDAO.findAll();
    }

    /// Get a single album using its id (Parameter: Int id)
    @Override
    @Transactional
    public Album getAlbumById(int id) {

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
    public void saveOrUpdate(Album newAlbum, Principal principal, boolean update, MultipartFile[] multipartFiles) throws IOException {

        if (!update) {
            Album album = new Album();
            User user = userDAO.findByUserName(principal.getName());
            
            album = newAlbum;
            album.setUser(user);
            album.setCreatedAt(Instant.now().plus(5, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES));
            albumDAO.saveOrUpdate(album);

            //Save the images input by user and connect them to album
			for (int i = 0 ; i < multipartFiles.length; i++) {
				Photo photo = new Photo();
				String fileName = StringUtils.cleanPath(multipartFiles[i].getOriginalFilename());
				photo.setName(fileName);
				photo.setAlbum(album);
				photoDAO.save(photo);

				String uploadDir = "images/album" + album.getId() + "/" + photo.getId();
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFiles[i]);
			}
        }
        else {
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
}
