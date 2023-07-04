package com.pamelamoreiras.bookstoremanager.users.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long id) {
        super(String.format("User with ID %s does not exists!", id));
    }
}
