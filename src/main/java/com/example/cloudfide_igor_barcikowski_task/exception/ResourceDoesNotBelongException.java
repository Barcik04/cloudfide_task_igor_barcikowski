package com.example.cloudfide_igor_barcikowski_task.exception;

public class ResourceDoesNotBelongException extends RuntimeException {
    public ResourceDoesNotBelongException(String message) {
        super(message);
    }
}
