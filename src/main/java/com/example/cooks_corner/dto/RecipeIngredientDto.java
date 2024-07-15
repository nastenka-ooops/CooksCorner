package com.example.cooks_corner.dto;

public record RecipeIngredientDto(
        Long ingredientId,
        String ingredientName,
        double amount,
        String measureUnit
) {
}
