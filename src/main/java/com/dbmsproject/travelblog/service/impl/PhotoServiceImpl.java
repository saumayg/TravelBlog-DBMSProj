package com.dbmsproject.travelblog.service.impl;

import java.io.IOException;
import java.security.Principal;

import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.dao.UserDAO;
import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Photo;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.PhotoService;
import com.dbmsproject.travelblog.utils.FileUploadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PhotoServiceImpl implements PhotoService{
    
    @Autowired
    private PhotoDAO photoDAO;

    @Autowired
    private AlbumDAO albumDAO;

    ///Save or update image
    @Override
    @Transactional
    public void saveOrUpdate(Principal principal, int albumId, MultipartFile[] multipartFiles) throws IOException {
        
        Album album = albumDAO.findById(albumId);

        if (album == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }
        
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
}
