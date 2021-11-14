package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import com.dbmsproject.travelblog.service.PhotoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

///Controller for image related services
@Controller
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public PhotoController(
        PhotoService photoService
    ) {
        this.photoService = photoService;
    }
    
    ///Save image
    @PostMapping("/save")
    public String save(
        @RequestParam(value = "image", required = false) MultipartFile[] multipartFiles,
        @RequestParam(value = "albumId", required = false) int id,
        Principal principal,
        Model model
    ) throws IOException {
        logger.info("PhotoController: Save incoming photo for album");

        photoService.save(principal, id, multipartFiles);
        return "redirect:/album/" + id;
    }

    ///Delete image from album
    @PostMapping("/delete/{id}")
    public String deletePhoto(
        @PathVariable int id,
        @RequestParam(value = "albumId") int albumId,
        Model model
    ) throws IOException {
        logger.info("PhotoController: Delete image from album");

        photoService.deleteById(id);
        return "redirect:/album/" + albumId;
    }
}
