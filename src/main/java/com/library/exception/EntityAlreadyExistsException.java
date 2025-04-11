package com.library.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String entity, String key, String value) {
        super("A " + entity + " with " + key + " '" + value + "' already exists");
    }
}