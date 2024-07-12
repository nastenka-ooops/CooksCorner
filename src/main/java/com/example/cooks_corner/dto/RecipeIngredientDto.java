package com.example.cooks_corner.dto;

public record RecipeIngredientDto(
        String ingredientName,
        double amount,
        String measure_unit
) {
}
