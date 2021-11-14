package com.dbmsproject.travelblog.service;

import java.util.List;

import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;

///Tag service interface
public interface TagService {

    ///Get all tags
    public List<Tag> getAll();

    ///Get all posts by the tag in sorted fashion(Parameter: int id)
    public List<Post> getAllPostsSorted(int id);

    ///Get tag according to its id (Parameter: int id)
    public Tag getTagById(int id);

    public void save(Tag tag);

    public void deleteById(int id);
}
