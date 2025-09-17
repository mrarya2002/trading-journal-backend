package com.tradingjournal.trading_journal.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageService {

    Cloudinary cloudinary;

    @Autowired
    public  ImageService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    public String uploadImage(MultipartFile image,String folder) throws IOException {
        if (image == null || image.isEmpty()) {
            return null;
        }
        String contentType = image.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new IllegalArgumentException("Only JPEG/PNG images are allowed");
        }
        if (image.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("Image size exceeds 5MB limit");
        }
        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "folder", folder,
                            "use_filename", true,
                            "unique_filename", true
                    ));
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new IOException("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }

    public void deleteImage(String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException e) {
            throw new IOException("Failed to delete image from Cloudinary: " + e.getMessage());
        }
    }

    private String extractPublicId(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String fileName = parts[parts.length - 1];
        String folder = parts[parts.length - 2];
        return folder + "/" + fileName.substring(0, fileName.lastIndexOf("."));
    }

}
