package com.example.cooks_corner.dto;

public record UserDto(
        String name,
        String bio,
        String email,
        String image_url
) {
}
