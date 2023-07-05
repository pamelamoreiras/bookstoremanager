package com.pamelamoreiras.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class UserAlreadyExistsException extends EntityExistsException {
    public UserAlreadyExistsException(final String email, final String username) {
        super(String.format("User with email %s or username %s already exists!", email, username));
    }
}
