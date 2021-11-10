package com.dbmsproject.travelblog.entity;

import java.time.Instant;
import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;

///Post entity 
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post")
public class Post {

	///Private key for Post entity (SQL: id)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

	///Title for post (SQL: title)
    @NotBlank(message = "Title is required")
    @Size(max = 20, message = "Character limit of 20 exceeded")
    @Column(name = "title", nullable = false)
    private String title;

    ///Short description for post (SQL: description)
    @NotBlank(message = " Description is required")
    @Size(min = 5, max = 50, message = "Number of characters should be between 5 and 50")
    @Column(name = "description", nullable = false)
    private String description;
    
    ///Body content for post (SQL: body)
    @NotBlank(message = "Body content is required")
    @Size(min = 5, message = "Minimum 5 characters required")
    @Column(name = "body", nullable = false)
    private String body;

    ///Time instant at which this post was created (SQL: created_at)
	@Column(name = "created_at")
	private Instant createdAt;
    
	///User who created the post (SQL: user_id, Many to one relationship)
    @ManyToOne(
		fetch = FetchType.EAGER,
		targetEntity = User.class
	)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /// Tags related to the post (SQL: Connected to table post_tag (post_id, tag_id) having many to many relationship)
    @ManyToMany(
		fetch = FetchType.LAZY,
		targetEntity = Tag.class,
		cascade = CascadeType.ALL
	)
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id") 
    )
    private List<Tag> tags = new ArrayList<>();

	/// List of comments under a post (One to many relationship with comment)
	@OneToMany(
		mappedBy = "post",
		fetch = FetchType.LAZY,
		targetEntity = Comment.class
	)
	private List<Comment> comments;

	@OneToOne(
		cascade = CascadeType.ALL,
		targetEntity = Album.class,
		fetch = FetchType.EAGER
	)
	@JoinColumn(name = "album_id", referencedColumnName = "id")
	private Album album;

	//Constructors

    public Post() {}
	
	public Post(
			@NotBlank(message = "Title is required") @Size(min = 3, message = "Minimum 3 characters required") String title,
			@NotBlank(message = " Description is required") @Size(min = 10, message = "Minimum 10 characters required") String description,
			@NotBlank(message = "Body content is required") @Size(min = 10, message = "Minimum 10 characters required") String body,
			Instant createdAt) {
		super();
		this.title = title;
		this.description = description;
		this.body = body;
		this.createdAt = createdAt;
	}

	public Post(
			@NotBlank(message = "Title is required") @Size(max = 20, message = "Character limit of 20 exceeded") String title,
			@NotBlank(message = " Description is required") @Size(min = 5, max = 50, message = "Number of characters should be between 5 and 50") String description,
			@NotBlank(message = "Body content is required") @Size(min = 5, message = "Minimum 5 characters required") String body,
			Instant createdAt, User user, List<Tag> tags, List<Comment> comments, Album album) {
		super();
		this.title = title;
		this.description = description;
		this.body = body;
		this.createdAt = createdAt;
		this.user = user;
		this.tags = tags;
		this.comments = comments;
		this.album = album;
	}

	//Getters and Setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}
	
	//To string method

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", description=" + description + ", body=" + body
				+ ", createdAt=" + createdAt + "]";
	}
}
