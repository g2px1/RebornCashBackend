package com.client.eurekaclient.config.errors;

import java.util.List;

public class ValidationErrorResponse {

    private List<Violation> violations;

    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }
}
