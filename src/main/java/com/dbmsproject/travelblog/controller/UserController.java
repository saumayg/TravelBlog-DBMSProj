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
import com.dbmsproject.travelblog.service.AlbumService;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;
import com.dbmsproject.travelblog.service.UserService;
import com.dbmsproject.travelblog.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.server.ResponseStatusException;

///Controller for user related services
@Controller
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    private final PostService postService;
    private final TagService tagService;
    private final AlbumService albumService;

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
        UserService userService,
        AlbumService albumService
    ) {
        this.tagService = tagService;
        this.postService = postService;
        this.userService = userService;
        this.albumService = albumService;
    }

    ///Private method to add common attributes to sidebar
    private Model addSidebarAttr(Model model) {

        //3 latest posts by all users
		List<Post> latestPosts = postService.getLatestPosts();
		model.addAttribute("latestPosts", latestPosts);

        //3 latest albums by all users
		List<Album> latestAlbums = albumService.getLatestAlbums();
		model.addAttribute("latestAlbums", latestAlbums);

        //List of all tags
		List<Tag> allTags = tagService.getAll();
		model.addAttribute("allTags", allTags);

        return model;
    }

    //checks whether the user accessing is owner or admin
    private boolean isPrincipalOwnerOrAdmin(Principal principal, String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        logger.info("User is admin : " + isAdmin);
        return principal != null && ( isAdmin || principal.getName().equals(username) );
    }

    ///Show user profile
    @GetMapping("/{username}")
    public String userProfile(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        logger.info("UserController: Show user profile page");

        //Get user details
        User user = userService.findByUserName(username);
        model.addAttribute("user", user);

        if (user == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, ""
            );
        }

        //3 latest posts by user
        model.addAttribute("latestPosts", userService.getAllLatestPostsSorted(username));

        //3 latest albums by user
        model.addAttribute("latestAlbums", userService.getAllLatestAlbumsSorted(username));

        //checks whether the user is the same as current user or admin
        if ( isPrincipalOwnerOrAdmin(principal, username) ) {
            model.addAttribute("owner", true);
        }
        else {
            model.addAttribute("owner", false);
        }

        //Update false 
        model.addAttribute("update", false);

        return "user/profile";
    }

    @GetMapping("/{username}/update")
    public String updateUserProfile(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        logger.info("UserController: Show user update form on profile page");

        //Get user details
        User user = userService.findByUserName(username);
        model.addAttribute("user", user);

        //checks whether the user is the same as current user or admin
        if ( isPrincipalOwnerOrAdmin(principal, username) ) {
            model.addAttribute("owner", true);
        }
        else {
            model.addAttribute("owner", false);
        }

        //Update is set true
        model.addAttribute("update", true);

        return "user/profile";
    }

    ///Show all posts bolonging to a user 
    @GetMapping("/{username}/posts")
    public String showAllPostByUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        logger.info("UserController: Show all posts belonging to a user");

        //Get all posts by current user
        List<Post> posts = userService.getAllPostsSorted(username);
        model.addAttribute("allPosts", posts);

        model.addAttribute("username", username);

        //checks whether the user is the same as current user
        if ( isPrincipalOwnerOrAdmin(principal, username) ) {
            model.addAttribute("owner", true);
        }
        else {
            model.addAttribute("owner", false);
        }
        
        //Adds 3 latest posts and all tags
        addSidebarAttr(model);
        
        return "user/allPosts";
    }

    ///Show all albums bolonging to a user 
    @GetMapping("/{username}/albums")
    public String showAllAlbumByUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) {
        logger.info("UserController: Show all albums belonging to a user");

        //Get all posts by current user
        List<Album> albums = userService.getAllAlbumsSorted(username);
        model.addAttribute("allAlbums", albums);

        model.addAttribute("username", username);

        //checks whether the user is the same as current user
        if( principal != null && principal.getName().equals(username)) {
            model.addAttribute("owner", true);
        }
        else {
            model.addAttribute("owner", false);
        }
        
        //Adds 3 latest posts and all tags
        addSidebarAttr(model);
        
        return "user/allAlbums";
    }

    @PostMapping("/update")
    public String updateUser(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindingResult,
        Model model
    ) throws IOException {
        logger.info("UserController: Update form data handler for user");

        if (bindingResult.hasErrors()) {
            logger.info("Could not validate user form");

            return "user/profile";
        }
        else {
            userService.save(user, true, null);
            return "redirect:/user/" + user.getUsername();
        }
    }

    @PostMapping("/update/profilePhoto")
    public String updateProfilePhoto(
        @RequestParam(value = "image", required = true) MultipartFile multipartFile,
        @RequestParam(value = "username", required = true) String username,
        Principal principal,
        Model model
    ) throws IOException {
        logger.info("UserController: Update profile photo handler for user");

        userService.updateProfilePhoto(principal, username, multipartFile);
        return "redirect:/user/" + username;
    }

    ///Delete user
    @PostMapping("/{username}/delete")
    public String deleteUser(
        @PathVariable String username,
        Principal principal,
        Model model
    ) throws IOException {
        logger.info("UserController: Delete user");

        userService.deleteById(username);

        //checks whether the user is the same as current user or admin
        if ( !isPrincipalOwnerOrAdmin(principal, username) ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        if ( !AppUtils.isAdmin() )
            SecurityContextHolder.clearContext();

        return "redirect:/";
    }
}
