package com.pamelamoreiras.bookstoremanager.publishers.exception;

import javax.persistence.EntityExistsException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PublisherAlreadyExistsException extends EntityExistsException {
    public PublisherAlreadyExistsException(final String name, final String code) {
        super(String.format("Publisher with name %s or code %s already exists!", name, code));
    }
}
