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
 
    public String uploadFile(MultipartFile file) {
        return uploadFile(file, "diversity"); 
    }

    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile file, String folder) {
        try {
            Map<String, Object> params = Map.of(
                    "folder", folder,
                    "resource_type", "auto");
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
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
