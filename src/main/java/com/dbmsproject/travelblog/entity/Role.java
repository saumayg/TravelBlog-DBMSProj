package com.dbmsproject.travelblog.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;

///Role entity
@EqualsAndHashCode()
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

	///Users under the role (Many to many relationship)
	@ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = User.class
	)
	@JoinTable(
		name = "user_role",
		joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
	)
	private List<User> users;
    
	//Constructors

    public Role() {}

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role(String name, List<User> users) {
		super();
		this.name = name;
		this.users = users;
	}

	//Getters and setters
	
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	//To string method
	
	public Role(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
