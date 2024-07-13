package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;

import java.util.List;

public record RecipeDto(
        Long id,
        String title,
        String description,
        String image_url,
        Category category,
        Difficulty difficulty,
        int cookingTimeMinutes,
        String author,
        boolean isLiked,
        int likesAmount,
        boolean isBookmarked,
        int bookmarksAmount,
        List<RecipeIngredientDto> ingredients
) {
}
