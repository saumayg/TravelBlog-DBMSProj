package com.dbmsproject.travelblog.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.dbmsproject.travelblog.dao.AlbumDAO;
import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumDAOImpl implements AlbumDAO {
    
    private EntityManager entityManager;

    @Autowired
    public AlbumDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /// Get all albums by all users
    @Override
    public List<Album> findAll() {
        
        Query query = entityManager.createQuery("select a from Album a order by a.createdAt desc");
		List<Album> albums = AppUtils.castList(Album.class, query.getResultList());
		
		return albums;
    }

    /// Get 3 latest albums among all users 
    @Override
    public List<Album> findLatestAlbum() {
        
        Query query = entityManager.createQuery("select a from Album a order by a.createdAt desc");
		query.setMaxResults(3);
        List<Album> albums = AppUtils.castList(Album.class, query.getResultList());
		
		return albums;
    }

    /// Get a single album using its id (Parameter: Int id)
    @Override
    public Album findById(int id) {
        
        Album album = entityManager.find(Album.class, id);

        return album;
    }

    ///Get all album by the user in sorted fashion(Parameter: String username)
    @Override
    public List<Album> allAlbumsSortedByUser(String username) {

        Query query = entityManager.createQuery("select a from Album a where a.user.username=:uName order by a.createdAt desc");
        query.setParameter("uName", username);

        List<Album> albums = AppUtils.castList(Album.class, query.getResultList());

        return albums;
    }

    ///Get latest 3 albums by the user in sorted fashion(Parameter: String username)
	@Override
    public List<Album> allLatestAlbumsSortedByUser(String username) {
        
        Query query = entityManager.createQuery("select a from Album a where a.user.username=:uName order by a.createdAt desc");
        query.setMaxResults(3);
        query.setParameter("uName", username);

        List<Album> albums = AppUtils.castList(Album.class, query.getResultList());

        return albums;
    }

    @Override
    public void saveOrUpdate(Album album) {

        Album dbAlbum = entityManager.merge(album);
        album.setId(dbAlbum.getId());
    }
}
