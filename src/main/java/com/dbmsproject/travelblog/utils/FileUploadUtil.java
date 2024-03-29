package com.dbmsproject.travelblog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public static void saveFile(
        String uploadDir,
        String fileName,
        MultipartFile multipartFile
    ) throws IOException {
        Path uploadFilePath = Paths.get(uploadDir);

        if (!Files.exists(uploadFilePath)) {
            Files.createDirectories(uploadFilePath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadFilePath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image : " + fileName, ioe);
        } 
    }

    public static void deleteFile(String uploadDir) throws IOException {
        Path deleteFilePath = Paths.get(uploadDir);
        FileSystemUtils.deleteRecursively(deleteFilePath);
        System.out.println("Successfully deleted");
    }
}
