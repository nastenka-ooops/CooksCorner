package com.example.cooks_corner.dto;

import java.util.List;

public record UserProfile(
        Long id,
        String name,
        String bio,
        String image_url,
        int recipesAmount,
        List<RecipeListDto> recipes,
        int followerAmount,
        int followingsAmount,
        List<RecipeListDto> savedRecipes
) {
}
