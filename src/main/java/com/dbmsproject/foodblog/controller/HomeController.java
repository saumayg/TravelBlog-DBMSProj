package com.dbmsproject.foodblog.controller;

import java.util.List;

import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final PostService postService;

	@Autowired
	public HomeController(PostService postService) {
		this.postService = postService;
	}
	
	@GetMapping("/")
	public String showHome(Model model) {
		List <Post> post = postService.getAll();
		model.addAttribute("post", post);
		return "home";
	}
}
