package com.dbmsproject.travelblog.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;

///Controller for post related services
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    //checks whether the user who created this post is the same as current user
    private boolean isPrincipalOwnerOfPost(Principal principal, Post post) {
        System.out.println(principal.getName());
        return principal != null && principal.getName().equals(post.getUser().getUsername());
    }

    ///show all posts by all users
    @GetMapping()
    public String showAllPost(Model model) {

        //All posts by all users
        List<Post> allPost = postService.getAll();
        model.addAttribute("allPost", allPost);

        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        //List of all tags
		List<Tag> allTag = tagService.getAll();
		model.addAttribute("allTag", allTag);

        return "blogEntries";
    }

    ///Show post detail page according to id
    @GetMapping("/{id}")
    public String showPostById(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        //Post according to given id
        Post postById = postService.getPostById(id);

        if (postById == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Page not found"
            );
        }

        model.addAttribute("postById", postById);

        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        //List of all tags
		List<Tag> allTag = tagService.getAll();
		model.addAttribute("allTag", allTag);

        //checks whether the user who created this post is the same as current user
        if( principal != null && isPrincipalOwnerOfPost(principal, postById) ) {
            model.addAttribute("username", principal.getName());
        }

        return "postDetail";
    }

    ///Show all posts belonging to a tag according to id
    @GetMapping("/tag/{id}")
    @PreAuthorize("hasRole('USER')")
    public String showPostByTagId(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        //All posts according to tag id
        List<Post> postByTagId = postService.getPostByTag(id);
        
        if (postByTagId == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Page not found"
            );
        }
        
        model.addAttribute("allPost", postByTagId);

        //Tag information
        Tag tagById = tagService.getTagById(id);
        model.addAttribute("tag", tagById);

        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        //List of all tags
		List<Tag> allTag = tagService.getAll();
		model.addAttribute("allTag", allTag);

        return "blogEntries";
    }

    ///Show all posts bolonging to a user 
    @GetMapping("/user/{username}")
    public String showAllPostByUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        //All posts by the user
        List<Post> allPostUser = postService.getPostByUsername(username);
        
        if (principal == null || !principal.getName().equals(username)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }
        
        model.addAttribute("allPost", allPostUser);
        
        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        //List of all tags
		List<Tag> allTag = tagService.getAll();
		model.addAttribute("allTag", allTag);
        
        return "userEntries";
    }

    ///Add a new post
    @PostMapping("/new")
    public String addNewPost(
        @Valid @ModelAttribute("post") Post post,
        BindingResult bindingResult,
        Principal principal,
        Model model
    ) {
        return "postForm";
    }
}
