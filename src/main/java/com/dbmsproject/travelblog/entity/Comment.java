package com.dbmsproject.travelblog.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;

///Comment Entity
@EqualsAndHashCode()
@Entity
@Table(name = "comment")
public class Comment {

    ///Private key for Comment entity (SQL: id)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    ///Body content for comment (SQL: body)
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 200, message = "Maximum 200 characters allowed")
    @Column(name = "body", nullable = false)
    private String body;

    ///Time instant at which comment was created (SQL: created_at)
    @Column(name = "created_at")
    private Instant createdAt;

    ///Post under which comment exists (SQL: post_id, Many to one relationship with post)
    @ManyToOne(
		fetch = FetchType.LAZY,
		targetEntity = Post.class
	)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    ///User who created comment (SQL: user_id, Many to one relationship with user)
    @ManyToOne(
		fetch = FetchType.EAGER,
		targetEntity = User.class
	)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    //Constructors
    
    public Comment() {}

	public Comment(
			@NotBlank(message = "Comment cannot be empty") @Size(max = 200, message = "Maximum 200 characters allowed") String body,
			Instant createdAt) {
		super();
		this.body = body;
		this.createdAt = createdAt;
	}

	public Comment(
			@NotBlank(message = "Comment cannot be empty") @Size(max = 200, message = "Maximum 200 characters allowed") String body,
			Instant createdAt, Post post, User user) {
		super();
		this.body = body;
		this.createdAt = createdAt;
		this.post = post;
		this.user = user;
	}
	
	//Getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
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
		return "Comment [id=" + id + ", body=" + body + ", createdAt=" + createdAt + "]";
	}
}
