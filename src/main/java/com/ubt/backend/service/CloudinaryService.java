package com.ubt.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploads a MultipartFile to Cloudinary under the "ubt-gallery" folder.
     * Returns the secure HTTPS URL of the uploaded image.
     */
    public String uploadImage(MultipartFile file) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder",          "ubt-gallery",
                        "resource_type",   "image",
                        "use_filename",    true,
                        "unique_filename", true
                )
        );
        return (String) result.get("secure_url");
    }
}
