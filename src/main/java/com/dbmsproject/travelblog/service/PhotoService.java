package com.dbmsproject.travelblog.service;

import java.io.IOException;
import java.security.Principal;

import org.springframework.web.multipart.MultipartFile;

///Photo service interface
public interface PhotoService {
    
    ///Save or update image
    public void saveOrUpdate(Principal principal, int albumId, MultipartFile[] multipartFiles) throws IOException;
}
