package com.dbmsproject.foodblog.controller;

import java.util.List;

import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

///Controller for post related services
@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    ///show all posts by all users
    @GetMapping("/post")
    public String showAllPost(Model model) {

        //All posts by all users
        List<Post> allPost = postService.getAll();
        model.addAttribute("allPost", allPost);

        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        return "blogEntries";
    }
}
