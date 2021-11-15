package com.dbmsproject.travelblog.controller;

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
import com.dbmsproject.travelblog.utils.AppUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

///Controller for tag related services
@Controller
@RequestMapping("/tag")
public class TagController {
    
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
    public TagController(
        TagService tagService,
        PostService postService,
        AlbumService albumService
    ) {
        this.tagService = tagService;
        this.postService = postService;
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

    @PostMapping("/save")
    public String newTag(
        @Valid @ModelAttribute("tag") Tag tag,
        BindingResult bindingResult,
        Principal principal,
        Model model
    ) {
        logger.info("TagController: Save tag form handler");

        if (bindingResult.hasErrors()) {
            logger.info("Could not validate tag");

            if ( !AppUtils.isAdmin() ) {
                throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, ""
                );
            }
    
            List<Tag> tags = tagService.getAll();
            model.addAttribute("allTags", tags);
    
            model.addAttribute("new", false);

            return "admin/allTags";
        }
        else {
            tagService.save(tag);
            return "redirect:/admin/tags";
        }
    }

    ///Show all posts belonging to a tag according to id
    ///Visible to all
    @GetMapping("/{id}/all")
    public String showPostByTagId(
        @PathVariable int id,
        Principal principal,
        Model model
    ) {
        logger.info("TagController: Show all posts under tag by id");

        //Tag information
        Tag tagById = tagService.getTagById(id);
        model.addAttribute("tag", tagById);

        //All posts
        List<Post> posts = tagService.getAllPostsSorted(id);
        model.addAttribute("allPosts", posts);

        //Adds 3 latest posts and all tags
        addSidebarAttr(model);

        return "tag/allPosts";
    }

    @PostMapping("/{id}/delete")
    public String deleteTag(
        @PathVariable int id,
        Model model
    ) {
        tagService.deleteById(id);
        return "redirect:/tag/all";
    }
}
