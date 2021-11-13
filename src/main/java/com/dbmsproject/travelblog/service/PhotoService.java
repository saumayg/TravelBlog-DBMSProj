package com.dbmsproject.travelblog.service;

import java.io.IOException;
import java.security.Principal;

import org.springframework.web.multipart.MultipartFile;

///Photo service interface
public interface PhotoService {
    
    ///Save image
    public void save(Principal principal, int albumId, MultipartFile[] multipartFiles) throws IOException;
}
