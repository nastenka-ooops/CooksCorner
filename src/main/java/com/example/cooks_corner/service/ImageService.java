package com.example.cooks_corner.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.exception.ImageUploadException;
import com.example.cooks_corner.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public ImageService(ImageRepository imageRepository, Cloudinary cloudinary) {
        this.imageRepository = imageRepository;
        this.cloudinary = cloudinary;
    }

    public Image uploadImage(MultipartFile file) {
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
        Image image = new Image(file.getOriginalFilename(), imageUrl);

        try {
            return imageRepository.save(image);
        } catch (Exception e) {
            throw new ImageUploadException("Failed to save image to the repository", e);
        }
    }
/*
    public List<ImageDto> getAllImages() {
        try {
            return imageRepository.findAll().stream()
                    .map(ImageMapper::mapToImageDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ImageRetrievalException("Failed to retrieve images from the repository", e);
        }
    }

    public List<ImageDto> downloadImagesFromCloudinary(String folder) {
        List<ImageDto> imageDTOs = new ArrayList<>();
        try {
            Map options = ObjectUtils.asMap(
                    "type", "upload",
                    "prefix", folder,
                    "max_results", 100
            );

            Map response = cloudinary.api().resources(options);

            List<String> imageUrls = new ArrayList<>();

            List<Map<String, Object>> resources = (List<Map<String, Object>>) response.get("resources");
            for (Map<String, Object> resource : resources) {
                String imageUrl = (String) resource.get("secure_url");
                imageUrls.add(imageUrl);
            }

            for (String url : imageUrls) {
                try {
                    Optional<Image> existingImage = imageRepository.findByUrl(url);
                    if (existingImage.isEmpty()) {
                        Image image = new Image();
                        image.setName(extractFileNameFromUrl(url));
                        image.setUrl(url);

                        Image savedImage = imageRepository.save(image);
                        imageDTOs.add(ImageMapper.mapToImageDto(savedImage));
                    }
                } catch (Exception e) {
                    throw new ImageRetrievalException("Failed to download images from Cloudinary", e);
                }
            }
        } catch (Exception e) {
            throw new ImageRetrievalException("Failed to fetch images from Cloudinary", e);
        }
        return imageDTOs;
    }*/

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
