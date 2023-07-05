package com.pamelamoreiras.bookstoremanager.publishers.exception;

import javax.persistence.EntityNotFoundException;

public class PublisherNotFoundException extends EntityNotFoundException {
    public PublisherNotFoundException(final Long id) {
        super(String.format("Publisher with id %s does not exists!", id));
    }
}
