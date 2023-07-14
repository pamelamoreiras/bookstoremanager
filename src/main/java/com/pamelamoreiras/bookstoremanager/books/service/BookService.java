package com.pamelamoreiras.bookstoremanager.books.service;

import com.pamelamoreiras.bookstoremanager.author.service.AuthorService;
import com.pamelamoreiras.bookstoremanager.books.mapper.BookMapper;
import com.pamelamoreiras.bookstoremanager.books.repository.BookRepository;
import com.pamelamoreiras.bookstoremanager.publishers.service.PublisherService;
import com.pamelamoreiras.bookstoremanager.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final BookMapper bookMapper = BookMapper.INSTANCE;

    private final BookRepository bookRepository;

    private final UserService userService;

    private final AuthorService authorService;

    private final PublisherService publisherService;
}
