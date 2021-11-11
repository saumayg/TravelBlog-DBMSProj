package com.dbmsproject.travelblog.dao.impl;

import javax.persistence.EntityManager;

import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.entity.Album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumDAOImpl implements AlbumDAO {
    
    private EntityManager entityManager;

    @Autowired
    public AlbumDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveOrUpdate(Album album) {

        Album dbAlbum = entityManager.merge(album);
        album.setId(dbAlbum.getId());
    }
}
