package com.dbmsproject.travelblog.dao.impl;

import javax.persistence.EntityManager;

import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.entity.Photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoDAOImpl implements PhotoDAO {
    
    private EntityManager entityManager;

    @Autowired
    public PhotoDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Photo photo) {
        Photo dbPhoto = entityManager.merge(photo);
        photo.setId(dbPhoto.getId());
    }
}
