package com.example.cooks_corner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<String> handleEmailAlreadyTakenException() {
        return new ResponseEntity<>("Email is already taken", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<String> handleImageUploadException() {
        return new ResponseEntity<>("Error occurred while uploading image", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRegistrationRequestException.class)
    public ResponseEntity<String> handleInvalidRegistrationRequestException() {
        return new ResponseEntity<>("Invalid registration request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCreatingRecipeRequestException.class)
    public ResponseEntity<String> handleInvalidCreatingRecipeRequestException() {
        return new ResponseEntity<>("Invalid creating recipe request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> handleRecipeNotFoundException() {
        return new ResponseEntity<>("Recipe not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<String> handleUserCreationException() {
        return new ResponseEntity<>("Error occurred while creating user", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException() {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException() {
        return new ResponseEntity<>("Invalid name or password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> handleDisabledException() {
        return new ResponseEntity<>("User is disable", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        return new ResponseEntity<>("Invalid JSON format for CreatingTourDto", HttpStatus.BAD_REQUEST);
    }
/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException() {
        return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
