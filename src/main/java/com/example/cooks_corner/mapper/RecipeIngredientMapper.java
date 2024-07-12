package com.example.cooks_corner.mapper;

import com.example.cooks_corner.dto.RecipeIngredientDto;
import com.example.cooks_corner.entity.RecipeIngredient;

public class RecipeIngredientMapper {
    public static RecipeIngredientDto mapToRecipeIngredientDto(RecipeIngredient recipeIngredient) {
        return new RecipeIngredientDto(recipeIngredient.getIngredient().getName(), recipeIngredient.getAmount(),
                recipeIngredient.getMeasureUnit());
    }
}
