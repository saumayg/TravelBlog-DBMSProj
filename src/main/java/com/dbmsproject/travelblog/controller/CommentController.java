package com.dbmsproject.travelblog.controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.dbmsproject.travelblog.entity.Comment;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.CommentService;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

///Controller for comment related services
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final TagService tagService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @Autowired
    public CommentController(
        CommentService commentService,
        PostService postService,
        TagService tagService
    ) {
        this.commentService = commentService;
        this.tagService = tagService;
        this.postService = postService;
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

    ///Save post method handler
    @PostMapping("/save")
    public String saveComment(
        @Valid @ModelAttribute("comment") Comment comment,
        BindingResult bindingResult,
        @RequestParam(value = "postId", required = true) int postId,
        Principal principal,
        Model model
    ) {
        logger.info("CommentController: Processing comment save");

        if (bindingResult.hasErrors()) {
            logger.info("CommentController: Could not validate comment");

            //Post according to given id
            Post postById = postService.getPostById(postId);
            model.addAttribute("postById", postById);

            //Adds 3 latest posts and all tags
            addSidebarAttr(model);

            //checks whether the user who created this post is the same as current user or admin access
            if(isPrincipalOwnerOfPostOrAdmin(principal, postById)) {
                model.addAttribute("owner", true);
            }

            //redirects back showing the error
            return "post/detail";
        }
        else {
            //save or update comment
            commentService.saveOrUpdate(comment, principal, postId);
            return "redirect:/post/" + postId;
        }
    }

    ///Delete comment
    @PostMapping("/delete/{id}")
    public String deleteComment(
        @PathVariable int id,
        @RequestParam("postId") int postId,
        Model model
    ) {
        logger.info("CommentController: Processing comment save");

        //Delete comment
        commentService.deleteById(id);
        return "redirect:/post/" + postId;
    }
}
