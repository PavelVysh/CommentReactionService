package com.facedynamics.comments.exeption;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class NotFoundException extends RuntimeException{
    private List<Error> errors;

    public NotFoundException(String s) {
        super(s);
    }
    public NotFoundException(List<Error> errors){
        this.errors = errors;
    }
}
