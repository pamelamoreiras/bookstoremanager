package com.pamelamoreiras.bookstoremanager.users.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(final Long id) {
        super(String.format("User with ID %s does not exists!", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("User with Username %s does not exists!", username));
    }
}
