package com.example.cooks_corner.exception;

public class InvalidCreatingRecipeRequestException extends RuntimeException {
    public InvalidCreatingRecipeRequestException(String message) {
        super(message);
    }
}
