package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.RecipeDto;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.service.RecipeService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(
        @Parameter(description = "ID of the recipe to be obtained. Cannot be empty.", example = "1") @PathVariable Long id) {
            return ResponseEntity.ok(recipeService.getRecipeById(id));
        }
}
