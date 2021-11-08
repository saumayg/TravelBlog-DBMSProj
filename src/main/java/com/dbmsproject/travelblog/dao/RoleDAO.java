package com.dbmsproject.travelblog.dao;

import com.dbmsproject.travelblog.entity.Role;

public interface RoleDAO {

    public Role findRoleByName(String roleName);

}
