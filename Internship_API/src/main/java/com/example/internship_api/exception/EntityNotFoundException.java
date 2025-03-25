package com.example.internship_api.exception;

import org.aspectj.lang.annotation.RequiredTypes;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(  Long id, Class<?> entity) {
        super("The " + entity.getSimpleName().toLowerCase() + " with id '" + id + "' does not exist in our records");
    }

}
