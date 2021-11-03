package com.dbmsproject.foodblog.entity;

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

import com.dbmsproject.foodblog.entity.audit.UserAudit;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post")
public class Post extends UserAudit{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Minimum 3 characters required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Body content is required")
    @Size(min = 10, message = "Minimum 10 characters required")
    @Column(name = "body", nullable = false)
    private String body;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id") 
    )
    private List<Tag> tag;

    public Post() {}

	public Post(
			@NotBlank(message = "Title is required") @Size(min = 3, message = "Minimum 3 characters required") String title,
			@NotBlank(message = "Body content is required") @Size(min = 10, message = "Minimum 10 characters required") String body) {
		this.title = title;
		this.body = body;
	}

	public Post(
			@NotBlank(message = "Title is required") @Size(min = 3, message = "Minimum 3 characters required") String title,
			@NotBlank(message = "Body content is required") @Size(min = 10, message = "Minimum 10 characters required") String body,
			@NotNull User user, List<Tag> tag) {
		this.title = title;
		this.body = body;
		this.user = user;
		this.tag = tag;
	}

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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
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

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", body=" + body + ", user=" + user + ", tag=" + tag + "]";
	}

    
}
