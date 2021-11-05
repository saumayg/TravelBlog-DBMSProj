package com.dbmsproject.foodblog.entity;

import java.time.Instant;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;

///Post entity 
///(Int id, String title, String description, String body, Instant createdAt, User user, List<Tag> tag)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post")
public class Post {

	private static final long serialVersionUID = 1L;

	///Private key for Post entity (SQL: id)
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

	///Title for post (SQL: title)
    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Minimum 3 characters required")
    @Column(name = "title", nullable = false)
    private String title;

    ///Short description for post (SQL: description)
    @NotBlank(message = " Description is required")
    @Size(min = 10, message = "Minimum 10 characters required")
    @Column(name = "description", nullable = false)
    private String description;
    
    ///Body content for post (SQL: body)
    @NotBlank(message = "Body content is required")
    @Size(min = 10, message = "Minimum 10 characters required")
    @Column(name = "body", nullable = false)
    private String body;

    ///Time instant at which this post was created (SQL: created_at)
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
    
	///User who created the post (SQL: user_id, Many to one relationship)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private User user;

    /// Tags related to the post (SQL: Connected to table post_tag (post_id, tag_id) having many to many relationship)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id") 
    )
    private List<Tag> tag;

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
			@NotBlank(message = "Title is required") @Size(min = 3, message = "Minimum 3 characters required") String title,
			@NotBlank(message = " Description is required") @Size(min = 10, message = "Minimum 10 characters required") String description,
			@NotBlank(message = "Body content is required") @Size(min = 10, message = "Minimum 10 characters required") String body,
			Instant createdAt, @NotNull User user, List<Tag> tag) {
		super();
		this.title = title;
		this.description = description;
		this.body = body;
		this.createdAt = createdAt;
		this.user = user;
		this.tag = tag;
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

	public List<Tag> getTag() {
		return tag;
	}

	public void setTag(List<Tag> tag) {
		this.tag = tag;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	//To string method

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", description=" + description + ", body=" + body
				+ ", createdAt=" + createdAt + ", user=" + user + ", tag=" + tag + "]";
	}

    
}
