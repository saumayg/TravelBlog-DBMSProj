package com.dbmsproject.travelblog.controller;

import java.util.List;
import java.util.logging.Logger;

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
	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	public HomeController(PostService postService, TagService tagService) {
		this.postService = postService;
		this.tagService = tagService;
	}

	///Private method to add common attributes to sidebar
    private Model addSidebarAttr(Model model) {

        //3 latest posts by all users
		List<Post> latestPosts = postService.getLatestPosts();
		model.addAttribute("latestPosts", latestPosts);

        //List of all tags
		List<Tag> allTags = tagService.getAll();
		model.addAttribute("allTags", allTags);

        return model;
    }
	
	///show home method
	///Visible to all
	@GetMapping("/")
	public String showHome(Model model) {
		logger.info("HomeController: Home page");
		
		//Any 3 random posts among all users
		List<Post> randomPosts = postService.getRandomPosts();
		model.addAttribute("randomPosts", randomPosts);
		
		addSidebarAttr(model);

		return "home";
	}
}
