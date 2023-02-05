package com.facedynamics.comments.exeptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class })
    protected ResponseEntity<Object> validationProblem(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ValidationException(
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        ), ex.getStatusCode());
    }
}
