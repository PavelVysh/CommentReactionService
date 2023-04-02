package com.facedynamics.comments.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {
    public static final String PROBLEMS = "problems";
    public static final String INTERNAL_ERROR_MESSAGE = "Internal error occurred";
    public static final String ERROR = "{} exception was thrown with message {}";
    Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(x -> errors.add(new Error(x.getDefaultMessage(), x.getField())));
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty(PROBLEMS, errors);
        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    protected ProblemDetail handleNotFoundException(NotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ProblemDetail handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException exc) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Message: " + exc.getMessage() +
                ". Field: " + exc.getName());
    }

    @ExceptionHandler(Exception.class)
    protected ProblemDetail handleGeneralException(Exception exc) {
        logger.error(ERROR, exc.getClass(), exc.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_MESSAGE);
    }
}
