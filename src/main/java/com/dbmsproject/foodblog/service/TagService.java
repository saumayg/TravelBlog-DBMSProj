package com.dbmsproject.foodblog.service;

import java.util.List;

import com.dbmsproject.foodblog.entity.Tag;

///Tag service interface
public interface TagService {

    ///Get all tags
    public List<Tag> getAll();

    ///Get tag according to its id (Parameter: int id)
    public Tag getTagById(int id);
}
