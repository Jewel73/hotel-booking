package edu.jewel.hotelbookingapp.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PhotoStorageService {

    private final String UPLOAD_DIR = "uploads/hotels";

    public Resource getPhoto(String filename) throws IOException {
        Path photoPath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        if (Files.exists(photoPath)) {
            return new UrlResource(photoPath.toUri());
        } else {
            throw new IOException("Photo not found: " + filename);
        }
    }


    public String savePhoto(MultipartFile photo) throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique file name and save the file
        String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(photo.getInputStream(), filePath);

        // Return the relative path for the saved photo
        return "/uploads/hotels/" + fileName;
    }
}
