package com.facedynamics.comments.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ProblemDetail validationProblem(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(x -> errors.add(new Error(x.getDefaultMessage(), x.getField())));
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("problems", errors);
        return problemDetail;
    }
    @ExceptionHandler(NotFoundException.class)
    protected ProblemDetail handleNotFoundException(NotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ProblemDetail handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("problems", new Error(ex.getMessage(), ex.getParameterName()));
        return  problemDetail;
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exc.getMessage());
    }
}
