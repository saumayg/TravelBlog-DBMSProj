package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;
import com.dbmsproject.travelblog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
// import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

		logger.info("UserController: Fetching latest posts");
        //3 latest posts by all users
		List<Post> latestPosts = postService.getLatestPosts();
		model.addAttribute("latestPosts", latestPosts);
		logger.info("UserController: Fetch successful");

		logger.info("UserController: Fetching all tags");
        //List of all tags
		List<Tag> allTags = tagService.getAll();
		model.addAttribute("allTags", allTags);
		logger.info("UserController: Fetch successful");

        return model;
    }

    @GetMapping("/{username}")
    public String userProfile(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        //Get user details
        User user = userService.findByUserName(username);
        model.addAttribute("user", user);

        model.addAttribute("latestPosts", userService.getAllLatestPostsSorted(username));

        model.addAttribute("latestAlbums", userService.getAllLatestAlbumsSorted(username));

        //checks whether the user is the same as current user
        if( principal != null && principal.getName().equals(username)) {
            model.addAttribute("owner", true);
        }

        model.addAttribute("update", false);

        return "userProfile";
    }

    @GetMapping("/{username}/update")
    public String updateUserProfile(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        //Get user details
        User user = userService.findByUserName(username);
        model.addAttribute("user", user);

        //checks whether the user is the same as current user
        if( principal != null && principal.getName().equals(username)) {
            model.addAttribute("owner", true);
        }

        model.addAttribute("update", true);

        return "userProfile";
    }

    ///Show all posts bolonging to a user 
    @GetMapping("/{username}/posts")
    public String showAllPostByUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        User user = userService.findByUserName(username);

        //Get all posts by current user
        List<Post> posts = userService.getAllPostsSorted(user.getUsername());
        model.addAttribute("allPosts", posts);

        model.addAttribute("username", username);

        //checks whether the user is the same as current user
        if( principal != null && principal.getName().equals(username)) {
            model.addAttribute("owner", true);
        }
        
        //Adds 3 latest posts and all tags
        addSidebarAttr(model);
        
        return "userEntries";
    }

    ///Show all albums bolonging to a user 
    @GetMapping("/{username}/albums")
    public String showAllAlbumByUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {

        //Get all posts by current user
        List<Album> albums = userService.getAllAlbumsSorted(username);
        model.addAttribute("allAlbums", albums);

        model.addAttribute("username", username);

        //checks whether the user is the same as current user
        if( principal != null && principal.getName().equals(username)) {
            model.addAttribute("owner", true);
        }
        
        //Adds 3 latest posts and all tags
        addSidebarAttr(model);
        
        return "allAlbums";
    }

    @PostMapping("/update")
    public String updateUser(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindingResult,
        Model model
    ) throws IOException {
        System.out.println("Update function");

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors().toString());

            return "userProfile";
        }
        else {
            userService.save(user, true, null);
            return "redirect:/user/" + user.getUsername();
        }
    }

    @PostMapping("/update/profilePhoto")
    public String updateProfilePhoto(
        @RequestParam(value = "image", required = false) MultipartFile multipartFile,
        @RequestParam(value = "username", required = false) String username,
        Principal principal,
        Model model
    ) throws IOException {
        userService.updateProfilePhoto(principal, username, multipartFile);
        return "redirect:/user/" + username;
    }
}
