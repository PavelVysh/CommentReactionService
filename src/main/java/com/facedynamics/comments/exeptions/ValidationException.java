package com.facedynamics.comments.exeptions;

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
