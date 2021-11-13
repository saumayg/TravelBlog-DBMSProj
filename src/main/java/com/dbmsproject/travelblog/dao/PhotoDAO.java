package com.dbmsproject.travelblog.dao;

import com.dbmsproject.travelblog.entity.Photo;

///Photo DAO Interface
public interface PhotoDAO {

    ///Get a photo by its id
    public Photo findById(int id);

    ///Save a photo
    public void save(Photo photo);

    ///Delete a photo
    public void deleteById(int id);
}
