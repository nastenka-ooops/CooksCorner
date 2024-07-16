package com.example.cooks_corner.dto;

public record UserListDto(
        Long id,
        String name,
        String bio,
        String imageUrl
) {
}
