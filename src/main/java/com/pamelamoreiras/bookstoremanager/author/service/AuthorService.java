package com.pamelamoreiras.bookstoremanager.author.service;

import com.pamelamoreiras.bookstoremanager.author.dto.AuthorDTO;
import com.pamelamoreiras.bookstoremanager.author.entity.Author;
import com.pamelamoreiras.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.author.exception.AuthorNotFoundException;
import com.pamelamoreiras.bookstoremanager.author.mapper.AuthorMapper;
import com.pamelamoreiras.bookstoremanager.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public AuthorDTO findById(final Long id) {
        final var foundAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
        return authorMapper.toDTO(foundAuthor);
    }

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void verifyIfExists(final String authorName) {
        authorRepository.findByName(authorName)
                .ifPresent(author -> {
                    throw new AuthorAlreadyExistsException(authorName);
                });
    }
}
