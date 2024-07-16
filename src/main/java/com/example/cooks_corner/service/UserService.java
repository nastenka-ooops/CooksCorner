package com.example.cooks_corner.service;

import com.example.cooks_corner.dto.UpdateUserProfileRequest;
import com.example.cooks_corner.dto.UserDto;
import com.example.cooks_corner.dto.UserProfile;
import com.example.cooks_corner.entity.AppUser;
import com.example.cooks_corner.exception.InvalidCreatingRecipeRequestException;
import com.example.cooks_corner.exception.UserNotFoundException;
import com.example.cooks_corner.mapper.RecipeMapper;
import com.example.cooks_corner.repository.AppUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final AppUserRepository userRepository;
    private final ImageService imageService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public UserService(AppUserRepository userRepository, ImageService imageService, ObjectMapper objectMapper, Validator validator) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    public AppUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username).orElseThrow(() ->
                new UsernameNotFoundException("user not found"));
    }

    public UserProfile getUserProfile() {
        AppUser user = loadUserByUsername(getCurrentUser());

        return new UserProfile(
                user.getId(),
                user.getName(),
                user.getBio(),
                user.getImage() != null ? user.getImage().getUrl() : null,
                user.getRecipes().size(),
                user.getRecipes().stream().map(RecipeMapper::mapToRecipeListDto).toList(),
                user.getFollowers().size(),
                user.getFollowings().size(),
                user.getBookmarkedRecipes().stream().map(RecipeMapper::mapToRecipeListDto).toList()
        );
    }

    public UserDto getUserById(Long id) {
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("user not found with id " + id);
        }
        AppUser userEntity = user.get();

        return new UserDto(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getBio(),
                userEntity.getImage() != null ? userEntity.getImage().getUrl() : null,
                userEntity.getRecipes().size(),
                userEntity.getRecipes().stream().map(RecipeMapper::mapToRecipeListDto).toList(),
                userEntity.getFollowers().size(),
                userEntity.getFollowings().size()
        );
    }

    public UserProfile updateUserProfile(String request, MultipartFile image) {
        UpdateUserProfileRequest userDto;

        try {
            userDto = objectMapper.readValue(request, UpdateUserProfileRequest.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON format for UpdateUserProfileRequest");
        }

        validateUpdateUserProfileRequest(userDto);

        AppUser user = loadUserByUsername(getCurrentUser());

        user.setName(userDto.name());
        user.setBio(userDto.bio());
        user.setImage(image != null ? imageService.uploadImage(image) : user.getImage());

        AppUser updatedUser = userRepository.save(user);
        return new UserProfile(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getBio(),
                updatedUser.getImage() != null ? updatedUser.getImage().getUrl() : null,
                updatedUser.getRecipes().size(),
                updatedUser.getRecipes().stream().map(RecipeMapper::mapToRecipeListDto).toList(),
                updatedUser.getFollowers().size(),
                updatedUser.getFollowings().size(),
                updatedUser.getBookmarkedRecipes().stream().map(RecipeMapper::mapToRecipeListDto).toList()
        );
    }

    public void confirmUser(AppUser user) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void validateUpdateUserProfileRequest(UpdateUserProfileRequest request) {
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "UpdateUserProfileRequest");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new InvalidCreatingRecipeRequestException("Invalid update user profile request " + bindingResult.getAllErrors());
        }
    }
}
