package com.pamelamoreiras.bookstoremanager.author.service;

import com.pamelamoreiras.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.pamelamoreiras.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.author.exception.AuthorNotFoundException;
import com.pamelamoreiras.bookstoremanager.author.mapper.AuthorMapper;
import com.pamelamoreiras.bookstoremanager.author.repository.AuthorRepository;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItShowBeCreated() {
        //given
        final var expectedAuthorToCreteDTO = authorDTOBuilder.buildAuthorDTO();
        final var expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreteDTO);

        //when
        when(authorRepository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        when(authorRepository.findByName(expectedCreatedAuthor.getName())).thenReturn(Optional.empty());

        final var createdAuthorDTO = authorService.create(expectedAuthorToCreteDTO);

        //then
        assertThat(createdAuthorDTO, is(IsEqual.equalTo(expectedAuthorToCreteDTO)));
    }

    @Test
    void whenExistingAuthorIsInformedThenExceptionShouldBeThrown() {
        //given
        final var expectedAuthorToCreteDTO = authorDTOBuilder.buildAuthorDTO();
        final var expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreteDTO);

        //when
        when(authorRepository.findByName(expectedCreatedAuthor.getName()))
                .thenReturn(Optional.of(expectedCreatedAuthor));

        assertThrows(AuthorAlreadyExistsException.class, () -> authorService.create(expectedAuthorToCreteDTO));
    }

    @Test
    void whenValidIdIsGivenThenAnAuthorShouldBeReturned() {

        final var expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        final var expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        when(authorRepository.findById(expectedFoundAuthor.getId()))
                .thenReturn(Optional.of(expectedFoundAuthor));

        final var foundAuthor = authorService.findById(expectedFoundAuthorDTO.getId());

        assertThat(foundAuthor, is(IsEqual.equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {

        final var expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        when(authorRepository.findById(expectedFoundAuthorDTO.getId()))
                .thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(expectedFoundAuthorDTO.getId()));
    }

    @Test
    void whenListAuthorsIsCalledThenItShouldBeReturned() {
        final var expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        final var expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        when(authorRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundAuthor));

        final var foundAuthorsDTO = authorService.findAll();

        assertThat(foundAuthorsDTO.size(), is(1));
        assertThat(foundAuthorsDTO.get(0), is(IsEqual.equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenListAuthorsIsCalledThenAnEmptyListShouldBeReturned() {

        when(authorRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        final var foundAuthorsDTO = authorService.findAll();

        assertThat(foundAuthorsDTO.size(), is(0));
    }
}
