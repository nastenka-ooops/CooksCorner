package com.example.cooks_corner.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cooks_corner.dto.ImageDto;
import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.exception.ImageUploadException;
import com.example.cooks_corner.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public ImageService(ImageRepository imageRepository, Cloudinary cloudinary) {
        this.imageRepository = imageRepository;
        this.cloudinary = cloudinary;
    }

    public ImageDto uploadImage(MultipartFile file) {
        Map params = ObjectUtils.asMap(
                "folder", "CooksCorner"
        );
        Map uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image to Cloudinary", e);
        }

        String imageUrl = (String) uploadResult.get("url");
        if (imageUrl == null) {
            throw new ImageUploadException("Failed to retrieve URL from Cloudinary response", null);
        }
        Image image = new Image(imageUrl, file.getOriginalFilename());

        try {
            imageRepository.save(image);
            return new ImageDto(image.getUrl(), image.getName());
        } catch (Exception e) {
            throw new ImageUploadException("Failed to save image to the repository", e);
        }
    }
}
