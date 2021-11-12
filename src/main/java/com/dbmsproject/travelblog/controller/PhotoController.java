package com.dbmsproject.travelblog.controller;

import java.io.IOException;
import java.security.Principal;

import com.dbmsproject.travelblog.service.PhotoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

///Controller for image related services
@Controller
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(
        PhotoService photoService
    ) {
        this.photoService = photoService;
    }
    
    @PostMapping("/save")
    public String save(
        @RequestParam(value = "image", required = false) MultipartFile[] multipartFiles,
        @RequestParam(value = "albumId", required = false) int id,
        Principal principal,
        Model model
    ) throws IOException {
        photoService.saveOrUpdate(principal, id, multipartFiles);
        return "redirect:/album/" + id;
    }
}
