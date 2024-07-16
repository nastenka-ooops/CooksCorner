package com.example.cooks_corner.dto;

public record RecipeListDto(
        Long id,
        String title,
        String author,
        String imageUrl,
        int likesAmount,
        int bookmarksAmount
) {
}
