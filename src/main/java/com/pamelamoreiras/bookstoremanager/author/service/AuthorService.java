package com.pamelamoreiras.bookstoremanager.author.service;

import com.pamelamoreiras.bookstoremanager.author.dto.AuthorDTO;
import com.pamelamoreiras.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.author.mapper.AuthorMapper;
import com.pamelamoreiras.bookstoremanager.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final static AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    private final AuthorRepository authorRepository;

    public AuthorDTO create(final AuthorDTO authorDTO) {

        verifyIfExists(authorDTO.getName());

        final var authorToCreate = authorMapper.toModel(authorDTO);
        final var createdAuthor = authorRepository.save(authorToCreate);
        return authorMapper.toDTO(createdAuthor);
    }

    private void verifyIfExists(String authorName) {
        authorRepository.findByName(authorName)
                .ifPresent(author -> {
                    throw new AuthorAlreadyExistsException(authorName);
                });
    }
}
