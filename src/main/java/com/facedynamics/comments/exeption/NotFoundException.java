package com.facedynamics.comments.exeption;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class NotFoundException extends RuntimeException{
    public NotFoundException(String s) {
        super(s);
    }
}
