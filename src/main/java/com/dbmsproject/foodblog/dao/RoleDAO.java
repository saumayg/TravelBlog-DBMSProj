package com.dbmsproject.foodblog.dao;

import com.dbmsproject.foodblog.entity.Role;

public interface RoleDAO {

    public Role findRoleByName(String roleName);

}
