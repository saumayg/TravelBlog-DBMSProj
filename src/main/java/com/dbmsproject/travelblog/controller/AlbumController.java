package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.dbmsproject.travelblog.entity.Album;
import com.dbmsproject.travelblog.entity.Post;
import com.dbmsproject.travelblog.entity.Tag;
import com.dbmsproject.travelblog.service.AlbumService;
import com.dbmsproject.travelblog.service.PostService;
import com.dbmsproject.travelblog.service.TagService;

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

///Controller for album related services
@Controller
@RequestMapping("/album")
public class AlbumController {
    
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
    public AlbumController(
        PostService postService, 
        TagService tagService,
        AlbumService albumService
    ) {
        this.postService = postService;
        this.tagService = tagService;
        this.albumService = albumService;
    }

    //checks whether the user who created this album is the same as current user or admin access
    private boolean isPrincipalOwnerOfAlbumOrAdmin(Principal principal, Album album) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        logger.info("User is admin : " + isAdmin);
        return principal != null && ( isAdmin || principal.getName().equals(album.getUser().getUsername()) );
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

    ///show all albums by all users
    ///Visible to all
    @GetMapping()
    public String showAllAlbums(Model model) {
        logger.info("AlbumController: Show all albums by all users");

        //All albums by all users
        List<Album> albums = albumService.getAll();
        model.addAttribute("allAlbums", albums);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        return "album/allAlbums";
    }

    ///Show album detail page according to id
    ///Visible to all
    @GetMapping("/{id}")
    public String showPostById(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        logger.info("AlbumController: Show album detail page according to id");

        //Post according to given id
        Album albumById = albumService.getAlbumById(id);
        model.addAttribute("albumById", albumById);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //checks whether the user who created this post is the same as current user
        if(isPrincipalOwnerOfAlbumOrAdmin(principal, albumById)) {
            model.addAttribute("owner", true);
        }
        else {
            model.addAttribute("owner", false);
        }

        return "album/detail";
    }

    ///Get mapping for new album , shows post form
    ///Allowed only when logged in
    @GetMapping("/new")
    public String showPostForm(Principal principal, Model model) {
        logger.info("AlbumController: Show new album form");

        //Throws forbidden exception when user not logged in
        if (principal == null) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //Add empty post attribute for saving
        model.addAttribute("album", new Album());

        //as save link, update is false
        model.addAttribute("update", false);

        return "album/form";
    }

    ///Get mapping for update album
    ///Allowed only when logged in and current user is the owner
    @GetMapping("/update/{id}")
    public String updatePostForm(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        logger.info("AlbumController: Show album form for update");

        //Get album according to its id
        Album album = albumService.getAlbumById(id);

        //Throws forbidden exception
        if (!isPrincipalOwnerOfAlbumOrAdmin(principal, album)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        //Adds current post info
        model.addAttribute("album", album);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        //update function
        model.addAttribute("update", true);

        return "album/form";
    }

    ///Save or update album
    @PostMapping("/save")
    public String saveOrUpdateAlbum(
        @Valid @ModelAttribute("album") Album album,
        BindingResult bindingResult,
        @RequestParam(value = "update", required = false) boolean update,
        @RequestParam(value = "image", required = false) MultipartFile[] multipartFiles,
        Principal principal,
        Model model
    ) throws IOException {
        logger.info("AlbumController: Processing new album form (Update = " + update + " )");

        //If validation errors return form
        if (bindingResult.hasErrors()) {
            logger.info("AlbumController: Could not validate album");

            //Adds 3 latest posts and all tags
            addSidebarAttr(model);

            model.addAttribute("update", update);
            
            return "album/form";
        }
        else {
            albumService.saveOrUpdate(album, principal, update, multipartFiles);
            return "redirect:/album/" + album.getId();
        }
    }

    ///Delete album
    @PostMapping("/delete/{id}")
    public String deleteAlbum(
        @PathVariable int id,
        Principal principal,
        Model model
    ) throws IOException {
        logger.info("AlbumController: Delete album according to its id");

        Album album = albumService.getAlbumById(id);

        //Throws forbidden exception
        if (!isPrincipalOwnerOfAlbumOrAdmin(principal, album)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, ""
            );
        }

        albumService.deleteById(id);
        return "redirect:/album";
    }
}
