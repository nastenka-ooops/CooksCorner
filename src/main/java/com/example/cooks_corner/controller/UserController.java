package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.UpdateUserProfileRequest;
import com.example.cooks_corner.dto.UserDto;
import com.example.cooks_corner.dto.UserProfile;
import com.example.cooks_corner.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints for user interaction")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get user by ID", description = "Returns user details based on the provided user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and details returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to fetch", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get current user's profile", description = "Returns the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found and returned"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @Operation(summary = "Update user profile", description = "Updates the profile of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "User not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public ResponseEntity<UserProfile> updateUser(
            @Parameter(description = "Profile data in JSON format", required = true)
            @RequestPart(name = "profile") String request,
            @Parameter(description = "Profile image file", required = false)
            @RequestPart(required = false, name = "image") MultipartFile image) {
        return ResponseEntity.ok(userService.updateUserProfile(request, image));
    }

    @Hidden
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> clearUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok("All users deleted successfully");
    }
}
