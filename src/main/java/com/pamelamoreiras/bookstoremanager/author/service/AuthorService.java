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
        final Author foundAuthor = verifyAndGetAuthor(id);
        return authorMapper.toDTO(foundAuthor);
    }

    private Author verifyAndGetAuthor(final Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void delete(final Long id) {
        verifyAndGetAuthor(id);
        authorRepository.deleteById(id);
    }

    private void verifyIfExists(final String authorName) {
        authorRepository.findByName(authorName)
                .ifPresent(author -> {
                    throw new AuthorAlreadyExistsException(authorName);
                });
    }
}
