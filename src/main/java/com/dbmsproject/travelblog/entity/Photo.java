package com.dbmsproject.travelblog.entity;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;

///Image entity
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "photo")
public class Photo {

    ///Private key for photo entity (SQL: id)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    ///File name for photo (SQL: name)
    @Column(name = "name")
    private String name;

    ///Album under which photos exist (SQL: album_id, Many to one relationship)
    @ManyToOne(
        fetch = FetchType.LAZY, 
        targetEntity = Album.class
    )
    @JoinColumn(
        name = "album_id",
        referencedColumnName = "id",
        nullable = true
    )
    private Album album;

    ///User which has this profile photo (One to one relationship)
    @OneToOne(
        mappedBy = "profilePhoto",
        targetEntity = User.class
    )
    private User user;

    //Constructors

    public Photo() {}

	public Photo(String name) {
		super();
		this.name = name;
	}

	public Photo(String name, Album album, User user) {
		super();
		this.name = name;
		this.album = album;
		this.user = user;
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

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public String getAlbumImagePath() {
		if (name == null)
			return null;
		return "images/album" + album.getId() + "/" + id + "/" + name;
	}

    //To string method

	@Override
	public String toString() {
		return "Photo [id=" + id + ", name=" + name + "]";
	}
}
