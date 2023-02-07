package com.facedynamics.comments.exeption;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ValidationException> validationProblem(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                new ValidationException(
                        ex.getBindingResult()
                                .getAllErrors()
                                .stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .toList()),
                ex.getStatusCode());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<String> constraintProblem() {
        return new ResponseEntity<>("Can't create a reply for a comment that doesn't exist.",
                HttpStatus.CONFLICT);
    }
    @ExceptionHandler(HttpMessageConversionException.class)
    protected ResponseEntity<String> parseProblem(HttpMessageConversionException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
    }
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
