package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.RecipeDto;
import com.example.cooks_corner.entity.AppUser;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.exception.RecipeNotFoundException;
import com.example.cooks_corner.mapper.RecipeIngredientMapper;
import com.example.cooks_corner.repository.AppUserRepository;
import com.example.cooks_corner.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final AppUserRepository userRepository;
    private final UserService userService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, AppUserRepository appUserRepository, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = appUserRepository;
        this.userService = userService;
    }

    public RecipeDto getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            throw new RecipeNotFoundException("Recipe not found with ID: " + id);
        }
        Recipe recipeEntity = recipe.get();
        String username = userService.getCurrentUser();
        AppUser user = userService.loadUserByUsername(username);
        boolean isLiked = isLiked(recipeEntity.getId(), user.getId());
        boolean isBookmarked = isBookmarked(recipeEntity.getId(), user.getId());

        return new RecipeDto(recipeEntity.getTitle(),
                recipeEntity.getDescription(),
                recipeEntity.getImage().getUrl(),
                recipeEntity.getCategory(),
                recipeEntity.getDifficulty(),
                recipeEntity.getCookingTimeMinutes(),
                recipeEntity.getUser().getName(),
                isLiked,
                recipeEntity.getLikes().size(),
                isBookmarked,
                recipeEntity.getBookmarks().size(),
                recipeEntity.getRecipeIngredients().stream().map(RecipeIngredientMapper::mapToRecipeIngredientDto).toList());
    }

    private boolean isLiked(Long recipeId, Long userId ){
        return userRepository.existsByIdAndLikedRecipes_Id(userId, recipeId);
    }
    private boolean isBookmarked(Long recipeId, Long userId ){
        return userRepository.existsByIdAndBookmarkedRecipes_id(userId, recipeId);
    }
}
