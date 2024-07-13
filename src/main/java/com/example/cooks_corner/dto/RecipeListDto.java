package com.example.cooks_corner.dto;

public record RecipeListDto(
        Long id,
        String title,
        String author,
        String image_url,
        int likesAmount,
        int bookmarksAmount
) {
}
