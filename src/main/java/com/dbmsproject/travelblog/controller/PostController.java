package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dbmsproject.travelblog.entity.Comment;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
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

    //checks whether the user who created this post is the same as current user or admin access
    private boolean isPrincipalOwnerOfPostOrAdmin(Principal principal, Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        logger.info("User is admin : " + isAdmin);
        return principal != null && ( isAdmin || principal.getName().equals(post.getUser().getUsername()) );
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

    ///show all posts by all users
    ///Visible to all
    @GetMapping()
    public String showAllPost(Model model) {
        logger.info("PostController: Show all posts by all users");

        //All posts by all users
        List<Post> allPosts = postService.getAll();
        model.addAttribute("allPosts", allPosts);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        return "post/allPosts";
    }

    ///Show post detail page according to id
    ///Visible to all
    @GetMapping("/{id}")
    public String showPostById(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        logger.info("PostController: Show post by id");

        //Post according to given id
        Post postById = postService.getPostById(id);
        model.addAttribute("postById", postById);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //checks whether the user who created this post is the same as current user or admin access
        if(isPrincipalOwnerOfPostOrAdmin(principal, postById) ) {
            model.addAttribute("owner", true);
        }

        //Sending an empty comment to use in new comment form
        model.addAttribute("comment", new Comment());

        return "post/detail";
    }

    ///Get mapping for new post , shows post form
    ///Allowed only when logged in
    @GetMapping("/new")
    public String showPostForm(Principal principal, Model model) {
        logger.info("PostController: Show new post form");

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

        //Send update false as this is for new post
        model.addAttribute("update", false);

        return "post/form";
    }

    ///Get mapping for update post
    ///Allowed only when logged in and current user is the owner or admin
    @GetMapping("update/{id}")
    public String updatePostForm(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        logger.info("PostController: Show update form");

        //Get post details
        Post post = postService.getPostById(id);

        //Throws forbidden exception when not logged in or user is not admin and not owner
        if (!isPrincipalOwnerOfPostOrAdmin(principal, post)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        //Adds current post info
        model.addAttribute("post", post);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //update is sent true
        model.addAttribute("update", true);

        return "post/form";
    }

    ///Add a new post, post method handler
    @PostMapping("/save")
    public String savePost(
        @Valid @ModelAttribute("post") Post post,
        BindingResult bindingResult,
        @RequestParam(value = "tgs", required = false) int[] tgs,
        @RequestParam(value = "albumName", required = true) String albumName,
        @RequestParam(value = "albumDescription", required = true) String albumDescription,
        @RequestParam(value = "update", required = false) boolean update,
        @RequestParam(value = "image", required = false) MultipartFile[] multipartFiles,
        Principal principal,
        Model model
    ) throws IOException {
        logger.info("PostController: Processing new post form");

        //If validation errors return form
        if (bindingResult.hasErrors()) {
            logger.info("Could not validate post");
            logger.info(bindingResult.getAllErrors().toString());

            //Adds 3 latest posts and all tags
            addSidebarAttr(model);

            model.addAttribute("update", update);
            
            return "post/form";
        }
        else {
            //save or update form
            postService.saveOrUpdate(post, principal, tgs, albumName, albumDescription, update, multipartFiles);
            return "redirect:/post/" + post.getId();
        }
    }

    ///Delete post
    @PostMapping("/delete/{id}")
    public String deletePost(
        @PathVariable int id,
        Model model
    ) throws IOException {
        logger.info("PostController: Delete a post by id");

        postService.deleteById(id);
        return "redirect:/post";
    }
}
