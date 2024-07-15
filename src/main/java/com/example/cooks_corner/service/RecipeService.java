package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.*;
import com.example.cooks_corner.entity.AppUser;
import com.example.cooks_corner.entity.Ingredient;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.entity.RecipeIngredient;
import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.exception.InvalidCreatingRecipeRequestException;
import com.example.cooks_corner.exception.RecipeNotFoundException;
import com.example.cooks_corner.mapper.RecipeIngredientMapper;
import com.example.cooks_corner.mapper.RecipeMapper;
import com.example.cooks_corner.repository.AppUserRepository;
import com.example.cooks_corner.repository.IngredientRepository;
import com.example.cooks_corner.repository.RecipeIngredientRepository;
import com.example.cooks_corner.repository.RecipeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final AppUserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final Validator validator;
    private final ObjectMapper objectMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, AppUserRepository appUserRepository, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, UserService userService, ImageService imageService, Validator validator, ObjectMapper objectMapper) {
        this.recipeRepository = recipeRepository;
        this.userRepository = appUserRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }

    public RecipeDto getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            throw new RecipeNotFoundException("Recipe not found with ID: " + id);
        }
        Recipe recipeEntity = recipe.get();

        return mapToRecipeDto(recipeEntity);
    }

    public List<RecipeListDto> getRecipeListByCategory(Category category) {
        return recipeRepository.findByCategory(category).stream()
                .map(RecipeMapper::mapToRecipeListDto).toList();
    }

    public List<RecipeListDto> searchRecipes(String query) {
        return recipeRepository.searchByQuery(query).stream()
                .map(RecipeMapper::mapToRecipeListDto).toList();
    }

    public RecipeDto createRecipe(String dto, MultipartFile image) {
        CreateRecipeRequest recipeDto;

        try {
            recipeDto = objectMapper.readValue(dto, CreateRecipeRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format for CreatingTourDto");
        }

        validateCreateRecipeRequest(recipeDto);

        AppUser user = userService.loadUserByUsername(userService.getCurrentUser());

        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.title());
        recipe.setDescription(recipeDto.description());
        recipe.setImage(imageService.uploadImage(image));
        recipe.setUser(user);
        recipe.setCategory(recipeDto.category());
        recipe.setDifficulty(recipeDto.difficulty());
        recipe.setCookingTimeMinutes(recipeDto.cookingTime());
        recipe.setLikes(new HashSet<>());
        recipe.setBookmarks(new HashSet<>());
        setRecipeIngredients(recipeDto.ingredients(), recipe);

        Recipe savedRecipe = recipeRepository.save(recipe);

        return mapToRecipeDto(savedRecipe);
    }

    public RecipeDto updateRecipe(String dto, MultipartFile image) {
        UpdateRecipeRequest recipeDto;

        try {
            recipeDto = objectMapper.readValue(dto, UpdateRecipeRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format for CreatingTourDto");
        }

        validateUpdateRecipeRequest(recipeDto);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeDto.id());
        if (recipeOptional.isEmpty()) {
            throw new RecipeNotFoundException("Recipe not found with ID: " + recipeDto.id());
        }

        Recipe recipeEntity = recipeOptional.get();

        AppUser user = userService.loadUserByUsername(userService.getCurrentUser());
        if (!user.equals(recipeEntity.getUser())){
            throw new InvalidCreatingRecipeRequestException("This user is not allowed to update this recipe");
        }

        recipeEntity.setTitle(recipeDto.title());
        recipeEntity.setDescription(recipeDto.description());
        recipeEntity.setImage(image != null ? imageService.uploadImage(image) : recipeEntity.getImage());
        recipeEntity.setCategory(recipeDto.category());
        recipeEntity.setDifficulty(recipeDto.difficulty());
        recipeEntity.setCookingTimeMinutes(recipeDto.cookingTime());
        recipeEntity.setLikes(new HashSet<>());
        recipeEntity.setBookmarks(new HashSet<>());

        deleteByRecipe(recipeEntity);
        setRecipeIngredients(recipeDto.ingredients(), recipeEntity);

        Recipe updatedRecipe = recipeRepository.save(recipeEntity);

        return mapToRecipeDto(updatedRecipe);
    }

    public RecipeDto likeRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new RecipeNotFoundException("Recipe not found with ID: " + id);
        }
        Recipe recipeEntity = recipeOptional.get();

        AppUser user = userService.loadUserByUsername(userService.getCurrentUser());

        if (recipeEntity.getLikes().contains(user)){
            recipeEntity.getLikes().remove(user);
        } else {
            recipeEntity.getLikes().add(user);
        }

        Recipe updatedRecipe = recipeRepository.save(recipeEntity);
        return mapToRecipeDto(updatedRecipe);
    }

    public RecipeDto bookmarkRecipe(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new RecipeNotFoundException("Recipe not found with ID: " + id);
        }
        Recipe recipeEntity = recipeOptional.get();

        AppUser user = userService.loadUserByUsername(userService.getCurrentUser());

        if (recipeEntity.getBookmarks().contains(user)){
            recipeEntity.getBookmarks().remove(user);
        } else {
            recipeEntity.getBookmarks().add(user);
        }

        Recipe updatedRecipe = recipeRepository.save(recipeEntity);
        return mapToRecipeDto(updatedRecipe);
    }

    private boolean isLiked(Long recipeId, Long userId) {
        return userRepository.existsByIdAndLikedRecipes_Id(userId, recipeId);
    }

    private boolean isBookmarked(Long recipeId, Long userId) {
        return userRepository.existsByIdAndBookmarkedRecipes_id(userId, recipeId);
    }

    private RecipeDto mapToRecipeDto(Recipe recipe) {
        String username = userService.getCurrentUser();
        AppUser user = userService.loadUserByUsername(username);
        boolean isLiked = isLiked(recipe.getId(), user.getId());
        boolean isBookmarked = isBookmarked(recipe.getId(), user.getId());

        return new RecipeDto(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDescription(),
                (recipe.getImage() != null) ? recipe.getImage().getUrl() : null,
                recipe.getCategory(),
                recipe.getDifficulty(),
                recipe.getCookingTimeMinutes(),
                recipe.getUser().getName(),
                isLiked,
                recipe.getLikes().size(),
                isBookmarked,
                recipe.getBookmarks().size(),
                recipe.getRecipeIngredients().stream().map(RecipeIngredientMapper::mapToRecipeIngredientDto).toList());
    }

    private void setRecipeIngredients(Set<RecipeIngredientDto> recipeIngredientDtos, Recipe recipe) {
        Set<RecipeIngredient> recipeIngredients = new HashSet<>();

        for (RecipeIngredientDto recipeIngredientDto : recipeIngredientDtos) {
            Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(recipeIngredientDto.ingredientName())
                    .orElseGet(() -> {
                        Ingredient newIngredient = new Ingredient(recipeIngredientDto.ingredientName());
                        return ingredientRepository.save(newIngredient);
                    });

            RecipeIngredient newRecipeIngredient = new RecipeIngredient(
                    recipeIngredientDto.amount(),
                    recipeIngredientDto.measureUnit(),
                    recipe,
                    ingredient
            );
            recipeIngredients.add(newRecipeIngredient);
        }
        recipe.getRecipeIngredients().addAll(recipeIngredients);
    }

    private void validateCreateRecipeRequest(CreateRecipeRequest request) {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "createRecipeRequest");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new InvalidCreatingRecipeRequestException("Invalid create recipe request " + bindingResult.getAllErrors());
        }
    }

    private void validateUpdateRecipeRequest(UpdateRecipeRequest request) {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "UpdateRecipeRequest");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new InvalidCreatingRecipeRequestException("Invalid update recipe request " + bindingResult.getAllErrors());
        }
    }

    void deleteByRecipe(Recipe recipe) {
        for (RecipeIngredient recipeIngredient : recipe.getRecipeIngredients()) {
            recipeIngredientRepository.deleteById(recipeIngredient.getId());
        }
        recipe.getRecipeIngredients().clear();
    }
}
