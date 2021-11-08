package com.dbmsproject.travelblog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;

///Role entity
///(int id, String name)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
public class Role {

	///Private key for role entity (SQL: id)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

	///Name of role (SQL: body, Current roles: User, Admin)
    @Column(name = "name")
    private String name;
    
	//Constructors

    public Role() {}

	public Role(String name) {
		this.name = name;
	}

	//Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//To string method

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
    
	    
}
