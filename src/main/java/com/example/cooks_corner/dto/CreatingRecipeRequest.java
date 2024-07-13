package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.RecipeIngredient;
import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record CreatingRecipeRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Difficulty cannot be null")
        Difficulty difficulty,

        @NotNull(message = "Category cannot be null")
        Category category,

        @Positive(message = "Cooking time must be positive")
        int cookingTime,

        @NotEmpty(message = "Ingredients cannot be empty")
        Set<RecipeIngredientDto> recipeIngredients
) {
}
