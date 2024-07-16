package com.example.cooks_corner.mapper;

import com.example.cooks_corner.dto.RecipeListDto;
import com.example.cooks_corner.entity.Recipe;

public class RecipeMapper {
    public static RecipeListDto mapToRecipeListDto(Recipe recipe) {
        return new RecipeListDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getUser().getName(),
                recipe.getImage() != null ? recipe.getImage().getUrl() : null,
                recipe.getLikes().size(),
                recipe.getBookmarks().size());
    }
}
