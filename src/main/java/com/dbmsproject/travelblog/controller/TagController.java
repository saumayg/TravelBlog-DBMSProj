package com.dbmsproject.travelblog.controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tag")
public class TagController {
    
    private final PostService postService;
    private final TagService tagService;
    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    public TagController(
        TagService tagService,
        PostService postService
    ) {
        this.tagService = tagService;
        this.postService = postService;
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

    ///Show all posts belonging to a tag according to id
    ///Visible to all
    @GetMapping("/{id}/all")
    public String showPostByTagId(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        logger.info("All posts under a tag");

        //Tag information
        Tag tagById = tagService.getTagById(id);
        model.addAttribute("tag", tagById);

        List<Post> posts = tagService.getAllPostsSorted(id);
        model.addAttribute("allPost", posts);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        return "blogEntries";
    }
}
