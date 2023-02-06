package com.facedynamics.comments.exeption;

import java.util.List;

public class ValidationException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }
    public List<String> getErrors() {
        return errors;
    }
}
