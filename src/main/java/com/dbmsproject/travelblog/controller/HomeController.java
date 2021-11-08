package com.dbmsproject.travelblog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;

///Controller for home page
@Controller
public class HomeController {

	private final PostService postService;
	
	private final TagService tagService;

	@Autowired
	public HomeController(PostService postService, TagService tagService) {
		this.postService = postService;
		this.tagService = tagService;
	}

	///Private method to add common attributes to sidebar
    private Model addSidebarAttr(Model model) {
        //3 latest posts by all users
		List<Post> latestPost = postService.getLatestPost();
		model.addAttribute("latestPost", latestPost);

        //List of all tags
		List<Tag> allTag = tagService.getAll();
		model.addAttribute("allTag", allTag);

        return model;
    }
	
	///show home method
	@GetMapping("/")
	public String showHome(Model model) {
		
		//Any 3 random posts among all users
		List<Post> randomPost = postService.getRandomPost();
		model.addAttribute("randomPost", randomPost);
		
		addSidebarAttr(model);

		return "home";
	}
}
