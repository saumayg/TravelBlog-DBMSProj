package com.dbmsproject.travelblog.service;

import java.util.List;

import com.dbmsproject.travelblog.entity.Tag;

///Tag service interface
public interface TagService {

    ///Get all tags
    public List<Tag> getAll();

    ///Get tag according to its id (Parameter: int id)
    public Tag getTagById(int id);
}
