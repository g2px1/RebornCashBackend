package com.client.authorizationService.config.errors;

import com.agk.nftGame.config.Violation;

import java.util.List;

public class ValidationErrorResponse {

    private List<Violation> violations;

    public ValidationErrorResponse(List<Violation> violations) {
        this.violations = violations;
    }
}
