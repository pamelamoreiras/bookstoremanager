package com.pamelamoreiras.bookstoremanager.author.exception;

import javax.persistence.EntityNotFoundException;

public class AuthorNotFoundException extends EntityNotFoundException {

    public AuthorNotFoundException(Long id) {
        super(String.format("Author with id %s does not exists!", id));
    }
}
