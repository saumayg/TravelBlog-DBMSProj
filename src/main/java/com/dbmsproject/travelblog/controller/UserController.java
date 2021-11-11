package com.dbmsproject.travelblog.controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;
import com.dbmsproject.travelblog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    private final PostService postService;
    private final TagService tagService;
    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    public UserController(
        TagService tagService,
        PostService postService,
        UserService userService
    ) {
        this.tagService = tagService;
        this.postService = postService;
        this.userService = userService;
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

    @GetMapping("/{username}")
    public String userProfile(
        @PathVariable String username,
        Model model
    ) {
        //Get user details
        User user = userService.findByUserName(username);
        model.addAttribute("user", user);

        return "userProfile";
    }

    ///Show all posts bolonging to a user 
    ///Page to be shown only to current user
    @GetMapping("/posts")
    public String showAllPostByUser(
        Principal principal,
        Model model
    ) {
        // Throws forbidden error when not logged in
        if (principal == null) {
            logger.info("Not logged in");
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        //Get user details
        User user = userService.findByUserName(principal.getName());
        model.addAttribute("user", user);
        
        //Adds 3 latest posts and all tags
        addSidebarAttr(model);
        
        return "userEntries";
    }
}
