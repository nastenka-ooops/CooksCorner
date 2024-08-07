package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.CreateRecipeRequest;
import com.example.cooks_corner.dto.RecipeDto;
import com.example.cooks_corner.dto.RecipeListDto;
import com.example.cooks_corner.dto.UpdateRecipeRequest;
import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipe", description = "Endpoints for recipe interaction")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Get a recipe by ID", description = "Returns a recipe by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(
            @Parameter(description = "ID of the recipe to be obtained. Cannot be empty.", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }
    @Operation(
            summary = "Like a recipe",
            description = "Adds a like to the specified recipe by the current user. If the recipe is already liked, it will be unliked.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully liked/unliked the recipe"),
                    @ApiResponse(responseCode = "404", description = "Recipe not found")
            }
    )
    @PostMapping("/{id}/like")
    public ResponseEntity<RecipeDto> likeRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.likeRecipe(id));
    }

    @Operation(
            summary = "Bookmark a recipe",
            description = "Adds a bookmark to the specified recipe by the current user. If the recipe is already bookmarked, the bookmark will be removed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully bookmarked/unbookmarked the recipe"),
                    @ApiResponse(responseCode = "404", description = "Recipe not found")
            }
    )
    @PostMapping("/{id}/bookmark")
    public ResponseEntity<RecipeDto> bookmarkRecipe(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.bookmarkRecipe(id));
    }

    @Operation(summary = "Get recipes by category", description = "Returns a list of recipes belonging to the specified category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of recipes"),
            @ApiResponse(responseCode = "400", description = "Invalid category provided")
    })
    @GetMapping("/by-category/{category}")
    public ResponseEntity<List<RecipeListDto>> getRecipeListByCategory(
            @Parameter(description = "Category of the recipes to be obtained. Cannot be empty.", example = "LUNCH")
            @PathVariable Category category) {
        List<RecipeListDto> recipes = recipeService.getRecipeListByCategory(category);
        return ResponseEntity.ok(recipes);
    }

    @Operation(summary = "Search for recipes", description = "Returns a list of recipes matching the search query.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of recipes"),
            @ApiResponse(responseCode = "404", description = "No recipes found matching the search query")
    })
    @GetMapping("/search")
    public ResponseEntity<List<RecipeListDto>> searchRecipes(
            @Parameter(description = "Search query for finding recipes. Cannot be empty.", example = "chocolate")
            @RequestParam String query) {
        List<RecipeListDto> recipes = recipeService.searchRecipes(query);
        if (recipes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipes);
    }

    @PostMapping
    @Operation(summary = "Create a new recipe", description = "Endpoint for creating a new recipe with an optional image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<RecipeDto> createRecipe(
            @Parameter(description = "Recipe data in JSON format", required = true,
                    schema = @Schema(implementation = CreateRecipeRequest.class))
            @RequestPart(name = "recipe") String recipe,
            @Parameter(description = "Optional image file for the recipe",
                    schema = @Schema(implementation = MultipartFile.class))
            @RequestPart(name = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(recipeService.createRecipe(recipe, image));
    }

    @Operation(
            summary = "Update a recipe",
            description = "Updates an existing recipe with new data and optionally a new image",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recipe successfully updated",
                            content = @Content(
                                    schema = @Schema(implementation = RecipeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recipe not found",
                            content = @Content
                    )
            }
    )
    @PutMapping()
    public ResponseEntity<RecipeDto> updateRecipe(
            @Parameter(
                    description = "Recipe data in JSON format",
                    required = true,
                    schema = @Schema(implementation = UpdateRecipeRequest.class)
            )
            @RequestPart(name = "recipe") String recipe,
            @Parameter(
                    description = "Optional image file for the recipe",
                    schema = @Schema(implementation = MultipartFile.class)
            )
            @RequestPart(name = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(recipeService.updateRecipe(recipe, image));
    }
}

