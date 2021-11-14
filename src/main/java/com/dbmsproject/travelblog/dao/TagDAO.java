package com.dbmsproject.travelblog.dao;

import java.util.List;

import com.dbmsproject.travelblog.entity.Tag;

///Tag DAO Interface
public interface TagDAO {

    ///Get all tags
    public List<Tag> findAll();

    ///Get tag according to its id (Parameter: int id)
    public Tag findById(int id);

    public void saveOrUpdate(Tag tag);

    public void deleteById(int id);
}
