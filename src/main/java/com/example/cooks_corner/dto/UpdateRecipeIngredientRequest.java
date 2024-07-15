package com.example.cooks_corner.dto;

public record UpdateRecipeIngredientRequest(
        Long ingredientId,
        String ingredientName,
        double amount,
        String measureUnit
) {
}
