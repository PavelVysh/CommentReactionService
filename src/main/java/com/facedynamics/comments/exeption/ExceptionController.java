package com.facedynamics.comments.exeption;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {
    @Value(value = "${application.name}")
    private String serviceName;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<List<Error>> validationProblem(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(x -> errors.add(new Error(x.getDefaultMessage(), serviceName)));
        return new ResponseEntity<>(new ValidationException(errors).getErrors(),
                ex.getStatusCode());
    }
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Error> constraintProblem() {
        return new ResponseEntity<>(new Error("Can't create a reply for non existing comment", serviceName),
                HttpStatus.CONFLICT);
    }
    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<Error> parseProblem(HttpMessageConversionException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(exc.getMessage(), serviceName));
    }
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Error> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(ex.getMessage(), serviceName));
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Error> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(ex.getMessage(), serviceName));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Error> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error("You must provide a valid JSON body", serviceName));
    }
}
