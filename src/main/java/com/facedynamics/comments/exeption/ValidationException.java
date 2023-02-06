package com.facedynamics.comments.exeption;

import java.time.LocalDateTime;

public class ValidationException {
    private final LocalDateTime localDateTime;
    private final String message;

    public ValidationException(String message) {
        this.localDateTime = LocalDateTime.now();
        this.message = message;
    }
    public LocalDateTime getLocalDateTime(){
        return localDateTime;
    }
    public String getMessage() {
        return message;
    }
}
