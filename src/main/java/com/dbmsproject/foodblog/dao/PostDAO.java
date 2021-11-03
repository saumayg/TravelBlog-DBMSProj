package com.dbmsproject.foodblog.dao;

import java.util.List;

import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.entity.Tag;

public interface PostDAO {
    
    // all posts
    public List<Post> findAll();

    // created by
    public List<Post> findByCreatedBy(int id); 

    // tag
    public List<Post> findByTag(List<Tag> tag);

    // delete
    public void deleteById(int id);

    // add / update
    public void saveOrUpdate(Post post);

    // post (id)
    public Post findById(int id);
}
