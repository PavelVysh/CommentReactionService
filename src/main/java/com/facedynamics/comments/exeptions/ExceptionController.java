package com.facedynamics.comments.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<?> validationProblem(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                new ValidationException(
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()),
                ex.getStatusCode());
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<?> constraintProblem(SQLIntegrityConstraintViolationException exc) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Can't create a reply for a comment that doesn't exist.");
    }
}
