package com.api.diversity.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to Cloudinary", e);
        }
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file from Cloudinary", e);
        }
    }
}
