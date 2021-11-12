package com.dbmsproject.travelblog.dao;

import com.dbmsproject.travelblog.entity.Photo;

public interface PhotoDAO {

    public Photo findById(int id);

    public void save(Photo photo);
}
