package com.dbmsproject.travelblog.service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.dbmsproject.travelblog.entity.Album;

import org.springframework.web.multipart.MultipartFile;

///Album service interface
public interface AlbumService {
    
    ///Get all albums by all users
    public List<Album> getAll();

    /// Get a single album using its id (Parameter: Int id)
    public Album getAlbumById(int id);

    ///Save or update album
    public void saveOrUpdate(Album album, Principal principal, boolean update, MultipartFile[] multipartFiles) throws IOException;
}
