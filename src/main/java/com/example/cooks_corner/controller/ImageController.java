package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.ImageDto;
import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/images")
@Tag(name = "Image", description = "Endpoint for uploading image to Cloudinary")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Upload Image to Cloudinary",
            description = "Uploads an image image to Cloudinary and saves it in the database.")
    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(
            @Parameter(description = "Image file to upload", required = true,
                    schema = @Schema(implementation = MultipartFile.class))
            @RequestParam("image") MultipartFile image) {
        Image uploadedImage = imageService.uploadImage(image);
        return ResponseEntity.ok(uploadedImage);
    }
}
