package com.devsuperior.dscatalog.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    //private static final serialVersionUID = 1L;

    public ResourceNotFoundException(String msg) {
        super(msg);

    }
}
