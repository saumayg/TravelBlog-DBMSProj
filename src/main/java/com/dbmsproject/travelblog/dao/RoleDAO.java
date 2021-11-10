package com.dbmsproject.travelblog.dao;

import com.dbmsproject.travelblog.entity.Role;

///Role DAO Interface
public interface RoleDAO {

    ///Find role using its name (Parameter: String rolename)
    public Role findRoleByName(String roleName);

}
