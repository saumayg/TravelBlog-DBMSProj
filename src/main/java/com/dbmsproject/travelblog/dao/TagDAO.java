package com.dbmsproject.foodblog.dao;

import java.util.List;

import com.dbmsproject.foodblog.entity.Tag;

///Tag DAO Interface
public interface TagDAO {

    ///Get all tags
    public List<Tag> findAll();

    ///Get tag according to its id (Parameter: int id)
    public Tag findById(int id);
}
