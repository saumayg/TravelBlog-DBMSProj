package com.dbmsproject.foodblog.controller;

import java.util.List;

import com.dbmsproject.foodblog.entity.Post;
import com.dbmsproject.foodblog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

///Controller for home page
@Controller
public class HomeController {

	private final PostService postService;

	@Autowired
	public HomeController(PostService postService) {
		this.postService = postService;
	}
	
	///show home method
	@GetMapping("/")
	public String showHome(Model model) {
		
		//Any 3 random posts among all users
		List<Post> randomPost = postService.getRandomPost();
		model.addAttribute("randomPost", randomPost);
		
		//3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);
		
		return "home";
	}
}
