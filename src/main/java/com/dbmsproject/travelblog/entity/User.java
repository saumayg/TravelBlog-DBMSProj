package com.dbmsproject.travelblog.entity;

import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;

///User entity
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class User {

	///Private key for user entity (SQL: id)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

	///Username for user (SQL: username)
	@NotBlank(message = "Username is required")
	@Size(min = 3, message = "Minimum 3 characters required")
    @Column(name = "username", nullable = false)
    private String username;

	///Password for user (SQL: password, stored as bcrypt encoding)
	@NotBlank(message = "Password is required")
	@Size(min = 5, message = "Minimum 5 characters required")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

	///First name of the user (SQL: first_name)
	@NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

	///Last name of the user (SQL: last_name)
	@NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

	///Email id of the user (SQL: email)
	@NotBlank(message = "Email is required")
	@Email(message = "Valid email is required")
    @Column(name = "email", nullable = false)
    private String email;

	///Phone number of the user (SQL: phone)
    @Column(name = "phone")
    private String phone;

	///Roles a user has (Many to many relationship with roles (USER, ADMIN))
    @ManyToMany(
		fetch = FetchType.EAGER, 
		cascade = CascadeType.ALL,
		targetEntity = Role.class
	)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") 
    )
    private Collection<Role> roles;

	/// List of comments under a user (One to many relationship with comment)
	@OneToMany(
		mappedBy = "user",
		targetEntity = Comment.class,
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Comment> comments;

	/// User profile photo (SQL: photo_id, One to one mapping)
	@OneToOne(
		cascade = CascadeType.ALL,
		targetEntity = Photo.class,
		fetch = FetchType.EAGER
	)
	@JoinColumn(
		name = "photo_id",
		referencedColumnName = "id"
	)
	private Photo profilePhoto;
    
	///Posts under the user (One to many mapping)
	@OneToMany(
		mappedBy = "user",
		targetEntity = Post.class,
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Post> posts;

	@OneToMany(
		mappedBy = "user",
		targetEntity = Album.class,
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL
	)
	private List<Album> albums;

	//Constructors

    public User() {}

	public User(
			@NotBlank(message = "Username is required") @Size(min = 3, message = "Minimum 3 characters required") String username,
			@NotBlank(message = "Password is required") @Size(min = 5, message = "Minimum 5 characters required") String password,
			@NotBlank(message = "First name is required") String firstName,
			@NotBlank(message = "Last name is required") String lastName,
			@NotBlank(message = "Email is required") @Email(message = "Valid email is required") String email,
			String phone) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	public User(
			@NotBlank(message = "Username is required") @Size(min = 3, message = "Minimum 3 characters required") String username,
			@NotBlank(message = "Password is required") @Size(min = 5, message = "Minimum 5 characters required") String password,
			@NotBlank(message = "First name is required") String firstName,
			@NotBlank(message = "Last name is required") String lastName,
			@NotBlank(message = "Email is required") @Email(message = "Valid email is required") String email,
			String phone, Collection<Role> roles, List<Comment> comments, Photo profilePhoto, List<Post> posts,
			List<Album> albums) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.roles = roles;
		this.comments = comments;
		this.profilePhoto = profilePhoto;
		this.posts = posts;
		this.albums = albums;
	}

	//Getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Photo getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(Photo profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	
	//To string method

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", phone=" + phone + "]";
	}
}
