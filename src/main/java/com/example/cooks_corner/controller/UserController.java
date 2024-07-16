package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.*;
import com.example.cooks_corner.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            @Parameter(description = "Profile data in JSON format", required = true,
            schema = @Schema(implementation = UpdateUserProfileRequest.class))
            @RequestPart(name = "profile") String request,
            @Parameter(description = "Profile image file", required = false)
            @RequestPart(required = false, name = "image") MultipartFile image) {
        return ResponseEntity.ok(userService.updateUserProfile(request, image));
    }

    @Operation(summary = "Get user's followers", description = "Returns a list of followers for the user with the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Followers found and returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserListDto>> getUserFollowers(
            @Parameter(description = "ID of the user to fetch followers for", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserFollowers(id));
    }

    @Operation(summary = "Get user's followings", description = "Returns a list of users that the user with the provided ID is following.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Followings found and returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/followings")
    public ResponseEntity<List<UserListDto>> getUserFollowings(
            @Parameter(description = "ID of the user to fetch followings for", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserFollowings(id));
    }
    @Operation(summary = "Search users", description = "Search for users based on the provided query.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found and returned"),
            @ApiResponse(responseCode = "404", description = "No users found")
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserListDto>> searchUser(
            @Parameter(description = "Search query for finding users", required = true)
            @RequestParam String query) {
        List<UserListDto> users = userService.searchUsers(query);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Follow a user", description = "Follow the user with the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully followed the user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/follow/{id}")
    public ResponseEntity<String> followUser(
            @Parameter(description = "ID of the user to follow", required = true)
            @PathVariable Long id) {
        userService.followUser(id);
        return ResponseEntity.ok("Now you are following user with id " + id);
    }

    @Operation(summary = "Unfollow a user", description = "Unfollow the user with the provided ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully unfollowed the user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/unfollow/{id}")
    public ResponseEntity<String> unfollowUser(
            @Parameter(description = "ID of the user to unfollow", required = true)
            @PathVariable Long id) {
        userService.unfollowUser(id);
        return ResponseEntity.ok("Now you are unfollowing user with id " + id);
    }
    @Hidden
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> clearUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok("All users deleted successfully");
    }
}
