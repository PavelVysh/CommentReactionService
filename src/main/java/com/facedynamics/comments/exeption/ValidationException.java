package com.facedynamics.comments.exeption;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ValidationException extends jakarta.validation.ValidationException {
    private final List<Error> errors;

    public ValidationException(List<Error> errors) {
        this.errors = errors;
    }

    public List<Error> getErrors() {
        return errors;
    }
}
