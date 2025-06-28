package com.api.diversity.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.Map;

import lombok.Data;

@Service
public class CloudinaryService {

    private final String rootFolder = "diversity";
    private final String subFolder = "images";

    @Data
    public static class CloudinaryResponse {
        private final String url;
        private final String publicId;
    }

    @Autowired
    private Cloudinary cloudinary;

    public CloudinaryResponse uploadFile(MultipartFile file) {
        return uploadFile(file, "diversity");
    }

    @SuppressWarnings("unchecked")
    public CloudinaryResponse uploadFile(MultipartFile file, String folder) {
        try {
            String fullFolderPath = rootFolder + "/" + subFolder + "/" + folder;
            Map<String, Object> params = Map.of(
                    "folder", fullFolderPath,
                    "resource_type", "auto");
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            return new CloudinaryResponse(
                    uploadResult.get("url").toString(),
                    uploadResult.get("public_id").toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo a cloudinary", e);
        }
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo en cloudinary", e);
        }
    }
}
