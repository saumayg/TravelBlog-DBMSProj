package com.dbmsproject.travelblog.controller;

import java.util.List;
import java.util.logging.Logger;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TagService tagService;
    private final PostService postService;
    private final UserService userService;
    private final AlbumService albumService;

    @Autowired
    public AdminController(
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

    private Logger logger = Logger.getLogger(getClass().getName());

    @GetMapping("/tags")
    public String showAllTags(
        Model model
    ) {
        logger.info("TagController: Show all tags to admin");

        if ( !AppUtils.isAdmin() ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        List<Tag> tags = tagService.getAll();
        model.addAttribute("allTags", tags);

        model.addAttribute("new", false);

        model.addAttribute("tag", new Tag());

        return "admin/allTags";
    }

    @GetMapping("/posts")
    public String showAllPosts(
        Model model
    ) {
        logger.info("TagController: Show all posts to admin");

        if ( !AppUtils.isAdmin() ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        List<Post> posts = postService.getAll();
        model.addAttribute("allPosts", posts);

        return "admin/allPosts";
    }

    @GetMapping("/users")
    public String showAllUsers(
        Model model
    ) {
        logger.info("TagController: Show all users to admin");

        if ( !AppUtils.isAdmin() ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        List<User> users = userService.findAll();
        model.addAttribute("allUsers", users);

        return "admin/allUsers";
    }
    
    @GetMapping("/albums")
    public String showAllAlbums(
        Model model
    ) {
        logger.info("TagController: Show all albums to admin");

        if ( !AppUtils.isAdmin() ) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        List<Album> albums = albumService.getAll();
        model.addAttribute("allAlbums", albums);

        return "admin/allAlbums";
    }
}
