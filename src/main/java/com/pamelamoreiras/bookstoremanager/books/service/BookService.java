package com.pamelamoreiras.bookstoremanager.books.service;

import com.pamelamoreiras.bookstoremanager.author.service.AuthorService;
import com.pamelamoreiras.bookstoremanager.books.dto.BookRequestDTO;
import com.pamelamoreiras.bookstoremanager.books.dto.BookResponseDTO;
import com.pamelamoreiras.bookstoremanager.books.entity.Book;
import com.pamelamoreiras.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.books.exception.BookNotFoundException;
import com.pamelamoreiras.bookstoremanager.books.mapper.BookMapper;
import com.pamelamoreiras.bookstoremanager.books.repository.BookRepository;
import com.pamelamoreiras.bookstoremanager.publishers.service.PublisherService;
import com.pamelamoreiras.bookstoremanager.users.dto.AuthenticatedUser;
import com.pamelamoreiras.bookstoremanager.users.entity.User;
import com.pamelamoreiras.bookstoremanager.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final BookMapper bookMapper = BookMapper.INSTANCE;

    private final BookRepository bookRepository;

    private final UserService userService;

    private final AuthorService authorService;

    private final PublisherService publisherService;

    public BookResponseDTO create(final AuthenticatedUser authenticatedUser, final BookRequestDTO bookRequestDTO) {
        final var foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        verifyIfBookIsAlreadyRegistered(foundAuthenticatedUser, bookRequestDTO);

        final var foundAuthor = authorService.verifyAndGetIfExists(bookRequestDTO.getAuthorId());
        final var foundPublisher = publisherService.verifyAndGetIfExists(bookRequestDTO.getPublisherId());

        var bookToSave = bookMapper.toModel(bookRequestDTO);
        bookToSave.setUser(foundAuthenticatedUser);
        bookToSave.setAuthor(foundAuthor);
        bookToSave.setPublisher(foundPublisher);

        final var savedBook = bookRepository.save(bookToSave);

        return bookMapper.toDTO(savedBook);
    }

    public BookResponseDTO findByIdAndUser(final AuthenticatedUser authenticatedUser, final Long bookId) {
        final var foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        return bookRepository.findByIdAndUser(bookId, foundAuthenticatedUser)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    public List<BookResponseDTO> findAllByUser(final AuthenticatedUser authenticatedUser) {
        final var foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());

        return bookRepository.findAllByUser(foundAuthenticatedUser).stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByIdAndUser(final AuthenticatedUser authenticatedUser, final Long bookId) {
        final var foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        final var foundBookToDelete = verifyAndGetIfExists(bookId, foundAuthenticatedUser);

        bookRepository.deleteByIdAndUser(foundBookToDelete.getId(), foundAuthenticatedUser);
    }

    private Book verifyAndGetIfExists(final Long bookId, final User foundAuthenticatedUser) {
        return bookRepository.findByIdAndUser(bookId, foundAuthenticatedUser)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    private void verifyIfBookIsAlreadyRegistered(final User foundUser, final BookRequestDTO bookRequestDTO) {
        bookRepository.findByNameAndIsbnAndUser(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), foundUser)
                .ifPresent(duplicatedBook -> {
                    throw new BookAlreadyExistsException(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), foundUser.getUsername());
                });
    }
}
