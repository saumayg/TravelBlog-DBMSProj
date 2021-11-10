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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;

///Tag entity
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tag")
public class Tag {

	///Private key for role entity (SQL: id)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

	///Name of tag (SQL: name)
    @NotBlank(message = "Tag name is required")
    @Column(name = "name", nullable = false)
    private String name;

	///Description of tag(SQL: description)
    @Column(name = "description")
    private String description;

	///Time instant when tag was created (SQL: created_at)
	@Column(name = "created_at")
	private Instant createdAt = Instant.now();

	///List of posts which have a particular tag (SQL: Connected to table post_tag (post_id, tag_id) having many to many relationship)
    @JsonIgnore
    @ManyToMany(
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		targetEntity = Post.class
	)
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id")
    )
    private List<Post> posts;

	//Constructors

    public Tag() {}

	public Tag(@NotBlank(message = "Tag name is required") String name, String description, Instant createdAt) {
		super();
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
	}

	public Tag(@NotBlank(message = "Tag name is required") String name, String description, Instant createdAt,
			List<Post> post) {
		super();
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
		this.posts = post;
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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> post) {
		this.posts = post;
	}

	//To string method
	
	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", description=" + description + ", createdAt=" + createdAt + "]";
	}
}
