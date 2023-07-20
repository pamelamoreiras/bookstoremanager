package com.pamelamoreiras.bookstoremanager.books.service;

import com.pamelamoreiras.bookstoremanager.author.entity.Author;
import com.pamelamoreiras.bookstoremanager.author.service.AuthorService;
import com.pamelamoreiras.bookstoremanager.books.builder.BookRequestDTOBuilder;
import com.pamelamoreiras.bookstoremanager.books.builder.BookResponseDTOBuilder;
import com.pamelamoreiras.bookstoremanager.books.entity.Book;
import com.pamelamoreiras.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.books.exception.BookNotFoundException;
import com.pamelamoreiras.bookstoremanager.books.mapper.BookMapper;
import com.pamelamoreiras.bookstoremanager.books.repository.BookRepository;
import com.pamelamoreiras.bookstoremanager.publishers.entity.Publisher;
import com.pamelamoreiras.bookstoremanager.publishers.service.PublisherService;
import com.pamelamoreiras.bookstoremanager.users.dto.AuthenticatedUser;
import com.pamelamoreiras.bookstoremanager.users.entity.User;
import com.pamelamoreiras.bookstoremanager.users.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorService authorService;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private BookService bookService;

    private BookRequestDTOBuilder bookRequestDTOBuilder;

    private BookResponseDTOBuilder bookResponseDTOBuilder;

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();
        authenticatedUser = new AuthenticatedUser("pamelamoreiras", "123456", "ADMIN");
    }

    @Test
    void whenNewBookIsInformedThenItShouldBeCreated() {
        final var expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        final var expectedCreatedBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        final var expectedCreatedBook = bookMapper.toModel(expectedCreatedBookDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(expectedBookToCreateDTO.getName()),
                eq(expectedBookToCreateDTO.getIsbn()),
                any(User.class))).thenReturn(Optional.empty());
        when(authorService.verifyAndGetIfExists(expectedBookToCreateDTO.getAuthorId())).thenReturn(new Author());
        when(publisherService.verifyAndGetIfExists(expectedBookToCreateDTO.getPublisherId())).thenReturn(new Publisher());
        when(bookRepository.save(any(Book.class))).thenReturn(expectedCreatedBook);

        final var createdBookResponseDTO = bookService.create(authenticatedUser, expectedBookToCreateDTO);

        assertThat(createdBookResponseDTO, Matchers.is(equalTo(expectedCreatedBookDTO)));
    }

    @Test
    void whenExistingBookIsInformedToCreateThenAnExceptionShouldBeThrown() {
        final var expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        final var expectedCreatedBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        final var expectedDuplicatedBook = bookMapper.toModel(expectedCreatedBookDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(expectedBookToCreateDTO.getName()),
                eq(expectedBookToCreateDTO.getIsbn()),
                any(User.class))).thenReturn(Optional.of(expectedDuplicatedBook));

        assertThrows(BookAlreadyExistsException.class, () -> bookService.create(authenticatedUser, expectedBookToCreateDTO));
    }

    @Test
    void whenExistingBookIsInformedThenABookShouldBeReturned() {
        final var expectedBookToFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        final var expectedFoundBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        final var expectedFoundBook = bookMapper.toModel(expectedFoundBookDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername()))
                .thenReturn(new User());
        when(bookRepository.findByIdAndUser(
                eq(expectedBookToFindDTO.getId()),
                any(User.class))).thenReturn(Optional.of(expectedFoundBook));

        final var foundBookDTO = bookService.findByIdAndUser(authenticatedUser, expectedBookToFindDTO.getId());

        assertThat(foundBookDTO, Matchers.is(equalTo(expectedFoundBookDTO)));
    }

    @Test
    void whenNotExistingBookIsInformedThenAnExceptionShouldBeThrown() {
        final var expectedBookToFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername()))
                .thenReturn(new User());
        when(bookRepository.findByIdAndUser(
                eq(expectedBookToFindDTO.getId()),
                any(User.class))).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.findByIdAndUser(authenticatedUser, expectedBookToFindDTO.getId()));
    }
}
