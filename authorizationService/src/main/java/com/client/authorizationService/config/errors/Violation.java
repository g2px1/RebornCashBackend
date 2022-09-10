package com.client.authorizationService.config.errors;

public class Violation {
    private String fieldName;
    private String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
