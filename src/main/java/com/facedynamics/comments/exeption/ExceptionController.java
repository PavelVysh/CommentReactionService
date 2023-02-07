package com.facedynamics.comments.exeption;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<?> validationProblem(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(x -> errors.add(new Error(x.getDefaultMessage(), ex.getClass().getName())));
        return new ResponseEntity<>(new ValidationException(errors).getErrors(),
                ex.getStatusCode());
    }
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Error> constraintProblem(ConstraintViolationException ex) {
        return new ResponseEntity<>(new Error("Can't create a reply for non existing comment", ex.getClass().getName()),
                HttpStatus.CONFLICT);
    }
    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<Error> parseProblem(HttpMessageConversionException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(exc.getMessage(), exc.getClass().getName()));
    }
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Error> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(ex.getMessage(), ex.getClass().getName()));
    }
}
