package com.ecommerce.ShopIT.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Create a unique public_id for the image
        String publicId = UUID.randomUUID().toString();

        // Upload with options
        Map<String, Object> options = ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "auto"
        );

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            System.err.println("Error uploading to Cloudinary: " + e.getMessage());
            throw e;
        }
    }
}