package com.ubt.backend.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ubt.backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try {

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty or null");
            }

            System.out.println("Uploading file: " + file.getOriginalFilename());

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "ubt-projects")
            );

            System.out.println("UPLOAD SUCCESS: " + uploadResult);

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            System.out.println("🔥 CLOUDINARY ERROR START 🔥");
            e.printStackTrace();
            System.out.println("🔥 CLOUDINARY ERROR END 🔥");

            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }
}