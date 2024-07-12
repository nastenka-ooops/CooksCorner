package com.example.cooks_corner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateRequest(
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
                message = "Password must be 8-15 characters long, contain upper and lower case letters, at least one digit, and one special character.")
        @NotBlank(message = "Password is mandatory")
        @Schema(description = "Password of the user.", example = "Password123!")
        String password
) {

}
