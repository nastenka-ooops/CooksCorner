package com.example.cooks_corner.exception;

public class InvalidRegistrationRequestException extends RuntimeException {
    public InvalidRegistrationRequestException(String message) {
        super(message);
    }
}
