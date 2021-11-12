package com.dbmsproject.travelblog.entity;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	///Time instant at which this album was created (SQL: created_at)
	@Column(name = "created_at")
	private Instant createdAt;

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
        targetEntity = Post.class,
		cascade = CascadeType.ALL
    )
    private Post post;

	@ManyToOne(
		fetch = FetchType.LAZY,
		targetEntity = User.class
	)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

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
			@Size(max = 100, message = "Character limit of 100 exceeded") String description, Instant createdAt,
			List<Photo> photos, Post post, User user) {
		super();
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.photos = photos;
		this.post = post;
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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	//To string method

	@Override
	public String toString() {
		return "Album [id=" + id + ", name=" + name + ", description=" + description + "]";
	};
}
