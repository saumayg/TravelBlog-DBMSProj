package com.dbmsproject.travelblog.dao.impl;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dbmsproject.travelblog.dao.PhotoDAO;
import com.dbmsproject.travelblog.entity.Photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

///Implementation of Photo DAO Interface
@Repository
public class PhotoDAOImpl implements PhotoDAO {
    
    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public PhotoDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    ///Get a photo by its id
    @Override
    public Photo findById(int id) {
        logger.info("PhotoDAO: findById(int id)");

        return entityManager.find(Photo.class, id);
    }

    ///Save a photo
    @Override
    public void save(Photo photo) {
        logger.info("PhotoDAO: save(Photo photo)");

        Photo dbPhoto = entityManager.merge(photo);
        photo.setId(dbPhoto.getId());
    }

    ///Delete a photo
    @Override
    public void deleteById(int id) {
        logger.info("PhotoDAO: deleteById(int id)");

        Query query = entityManager.createQuery("delete from Photo where id=:photoId");
        query.setParameter("photoId", id);

        query.executeUpdate();
    }
}
