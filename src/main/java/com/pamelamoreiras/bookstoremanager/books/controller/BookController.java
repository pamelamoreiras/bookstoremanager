package com.pamelamoreiras.bookstoremanager.books.controller;

import com.pamelamoreiras.bookstoremanager.books.dto.BookRequestDTO;
import com.pamelamoreiras.bookstoremanager.books.dto.BookResponseDTO;
import com.pamelamoreiras.bookstoremanager.books.service.BookService;
import com.pamelamoreiras.bookstoremanager.users.dto.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController implements BookControllerDocs {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO create(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                  @RequestBody @Valid BookRequestDTO bookRequestDTO) {
        return bookService.create(authenticatedUser, bookRequestDTO);
    }
}
