package com.facedynamics.comments.exeption;

public class CustomException {
    private final String error;

    public CustomException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
