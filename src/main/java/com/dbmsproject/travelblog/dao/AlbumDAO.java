package com.dbmsproject.travelblog.dao;

import java.util.List;

import com.dbmsproject.travelblog.entity.Album;

///Album DAO Interface
public interface AlbumDAO {

    /// Get all albums by all users
    public List<Album> findAll();

    /// Get 3 latest albums among all users 
    public List<Album> findLatestAlbum();

    /// Get a single album using its id (Parameter: Int id)
    public Album findById(int id);

    ///Get all album by the user in sorted fashion(Parameter: String username)
    public List<Album> allAlbumsSortedByUser(String username);

    ///Get latest 3 albums by the user in sorted fashion(Parameter: String username)
	public List<Album> allLatestAlbumsSortedByUser(String username);

    ///add or update album
    public void saveOrUpdate(Album album);

    ///Delete an album
    public void deleteById(int id);
}
