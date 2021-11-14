package com.dbmsproject.travelblog.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Photo;
import com.dbmsproject.travelblog.service.PhotoService;
import com.dbmsproject.travelblog.utils.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

///Implementation for Photo Service Interface
@Service
public class PhotoServiceImpl implements PhotoService{
    
    @Autowired
    private PhotoDAO photoDAO;

    @Autowired
    private AlbumDAO albumDAO;

    private Logger logger = Logger.getLogger(getClass().getName());

    ///Save image
    @Override
    @Transactional
    public void save(Principal principal, int albumId, MultipartFile[] multipartFiles) throws IOException {
        logger.info("PhotoService: save(Principal principal, int albumId, MultipartFile[] multipartFiles)");

        //Get album for photo
        Album album = albumDAO.findById(albumId);

        //Throws exception if album not found
        if (album == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }
        
        //Saves photos
        for (int i = 0 ; i < multipartFiles.length; i++) {
            if (multipartFiles[i].isEmpty())
                continue;
            //New photo
            Photo photo = new Photo();
            String fileName = StringUtils.cleanPath(multipartFiles[i].getOriginalFilename());
            photo.setName(fileName);
            photo.setAlbum(album);
            photoDAO.save(photo);

            //Save in directory
            String uploadDir = "images/user" + album.getUser().getId() + "/album" + albumId + "/" + photo.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFiles[i]);
        }
    }

    ///Delete image
    @Override
    @Transactional
    public void deleteById(int id) throws IOException {
        logger.info("PhotoService: deleteById(int id)");

        //Get photo by id
        Photo photo = photoDAO.findById(id);
        
        //Throws exception if photo not found
        if (photo == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }

        Album album = photo.getAlbum();

        //Throws exception if album not found
        if (album == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }

        //Delete photo directory
        String deleteDir = "images/user" + album.getUser().getId() + "/album" + album.getId() + "/" + id;
        FileUploadUtil.deleteFile(deleteDir);

        //Delete photo
        photoDAO.deleteById(id);
    }
}
