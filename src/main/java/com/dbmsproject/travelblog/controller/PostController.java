package com.dbmsproject.foodblog.controller;

import java.security.Principal;
import java.util.List;

import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.entity.Tag;
import com.dbmsproject.foodblog.service.PostService;
import com.dbmsproject.foodblog.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("postById", postById);

        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        //checks whether the user who created this post is the same as current user
        if( principal != null && isPrincipalOwnerOfPost(principal, postById) ) {
            model.addAttribute("username", principal.getName());
        }

        return "postDetail";
    }

    ///Show all posts belonging to a tag according to id
    @GetMapping("/tag/{id}")
    public String showPostByTagId(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        //All posts according to tag id
        List<Post> postByTagId = postService.getPostByTag(id);
        model.addAttribute("allPost", postByTagId);

        //Tag information
        Tag tagById = tagService.getTagById(id);
        model.addAttribute("tag", tagById);

        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        return "blogEntries";
    }
}
