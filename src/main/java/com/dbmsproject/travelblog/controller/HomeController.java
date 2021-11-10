package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dbmsproject.travelblog.entity.Photo;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;
import com.dbmsproject.travelblog.utils.FileUploadUtil;

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

	@GetMapping("/photo")
	public String imageForm(Model model) {
		model.addAttribute("photo", new Photo());

		return "photo";
	}

	@PostMapping("/photo/save")
	public String saveImage(
		@Valid @ModelAttribute("photo") Photo photo,
		BindingResult bindingResult,
		@RequestParam(value = "image", required = false) MultipartFile multipartFile
	) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		photo.setName(fileName);

		String uploadDir = "images/" + photo.getId();

		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		return "home";
	}
}
