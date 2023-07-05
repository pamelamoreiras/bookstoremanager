package com.pamelamoreiras.bookstoremanager.author.exception;

import javax.persistence.EntityExistsException;

public class AuthorAlreadyExistsException extends EntityExistsException {
    public AuthorAlreadyExistsException(final String name) {
        super(String.format("User with name %s already exists!", name));
    }
}
