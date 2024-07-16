package com.example.cooks_corner.dto;

import jakarta.validation.constraints.Max;

public record UpdateUserProfileRequest(
        String name,
        @Max(255)
        String bio
) {
}
