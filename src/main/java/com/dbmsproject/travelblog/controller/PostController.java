package com.dbmsproject.travelblog.controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.entity.User;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;

///Controller for post related services
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    public PostController(
        PostService postService, 
        TagService tagService
    ) {
        this.postService = postService;
        this.tagService = tagService;
    }

    //checks whether the user who created this post is the same as current user
    private boolean isPrincipalOwnerOfPost(Principal principal, Post post) {
        System.out.println(principal.getName());
        return principal != null && principal.getName().equals(post.getUser().getUsername());
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

    ///show all posts by all users
    ///Visible to all
    @GetMapping()
    public String showAllPost(Model model) {

        //All posts by all users
        List<Post> allPost = postService.getAll();
        model.addAttribute("allPost", allPost);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        return "blogEntries";
    }

    ///Show post detail page according to id
    ///Visible to all
    @GetMapping("/{id}")
    public String showPostById(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        //Post according to given id
        Post postById = postService.getPostById(id);
        model.addAttribute("postById", postById);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //checks whether the user who created this post is the same as current user
        if( principal != null && isPrincipalOwnerOfPost(principal, postById) ) {
            model.addAttribute("owner", true);
        }

        return "postDetail";
    }

    ///Show all posts belonging to a tag according to id
    ///Visible to all
    @GetMapping("/tag/{id}")
    @PreAuthorize("hasRole('USER')")
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

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        return "blogEntries";
    }

    ///Show all posts bolonging to a user 
    ///Page to be shown only to user
    @GetMapping("/user/{username}")
    public String showAllPostByUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        //All posts by the user
        List<Post> allPostUser = postService.getPostByUsername(username);
        // Throws forbidden error when not logged in or, current user is not owner
        if (principal == null || !principal.getName().equals(username)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }
        model.addAttribute("allPost", allPostUser);
        
        //Adds 3 latest posts and all tags
        addSidebarAttr(model);
        
        return "userEntries";
    }

    ///Get mapping for new post , shows post form
    ///Allowed only when logged in
    @GetMapping("/new")
    public String showPostForm(Principal principal, Model model) {

        //Throws forbidden exception when user not logged in
        if (principal == null) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //Add empty post attribute for saving
        model.addAttribute("post", new Post());

        //add function
        model.addAttribute("update", false);

        return "postForm";
    }

    ///Get mapping for update post
    ///Allowed only when logged in and current user is the owner
    @GetMapping("update/{id}")
    public String updatePostForm(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {

        Post post = postService.getPostById(id);
        User user = post.getUser();

        //Throws forbidden exception
        if (principal == null || !principal.getName().equals(user.getUsername())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        System.out.println(post.getId());

        //Adds current post info
        model.addAttribute("post", post);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //update function
        model.addAttribute("update", true);

        return "postForm";
    }

    ///Add a new post, post method handler
    @PostMapping("/save")
    public String addNewPost(
        @Valid @ModelAttribute("post") Post post,
        @RequestParam(value = "tgs", required = false) int[] tgs,
        @RequestParam(value = "update", required = false) boolean update,
        BindingResult bindingResult,
        Principal principal,
        Model model
    ) {
        logger.info("Processing new post form");
        // logger.info(model.getAttribute("type").toString());
        System.out.println(update);

        //If validation errors return form
        if (bindingResult.hasErrors()) {
            logger.info("Could not validate post");
            logger.info(bindingResult.getAllErrors().toString());

            //Adds 3 latest posts and all tags
            addSidebarAttr(model);
            
            return "postForm";
        }
        else {
            //save or update form
            postService.saveOrUpdate(post, principal, tgs, update);
            return "redirect:/post/" + post.getId();
        }
    }
}
