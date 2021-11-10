package com.dbmsproject.travelblog.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;

///Album entity
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "album")
public class Album {
    
    ///Private key for album entity (SQL: id)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    ///Name of album (SQL: name)
    @NotBlank(message = "Name is required")
    @Size(max = 64, message = "Character limit of 64 exceeded")
    @Column(name = "name")
    private String name;

    ///Description of album (SQL: description)
    @Size(max = 100, message = "Character limit of 100 exceeded")
    @Column(name = "description")
    private String description;

    ///List of photos under an album (One to many relationship)
    @OneToMany(
        mappedBy = "album",
        targetEntity = Photo.class,
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    private List<Photo> photos;

    ///Post under which album was created (One to one relationship)
    @OneToOne(
        mappedBy = "album",
        targetEntity = Post.class
    )
    private Post post;

    //Constructors

    public Album() {}

	public Album(
			@NotBlank(message = "Name is required") @Size(max = 64, message = "Character limit of 64 exceeded") String name,
			@Size(max = 100, message = "Character limit of 100 exceeded") String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public Album(
			@NotBlank(message = "Name is required") @Size(max = 64, message = "Character limit of 64 exceeded") String name,
			@Size(max = 100, message = "Character limit of 100 exceeded") String description, List<Photo> photos,
			Post post) {
		super();
		this.name = name;
		this.description = description;
		this.photos = photos;
		this.post = post;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

    //To string method

	@Override
	public String toString() {
		return "Album [id=" + id + ", name=" + name + ", description=" + description + "]";
	};
}
