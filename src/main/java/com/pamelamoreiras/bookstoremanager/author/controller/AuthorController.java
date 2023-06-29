package com.pamelamoreiras.bookstoremanager.author.controller;

import com.pamelamoreiras.bookstoremanager.author.dto.AuthorDTO;
import com.pamelamoreiras.bookstoremanager.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/authors")
@RequiredArgsConstructor
public class AuthorController implements AuthorControllerDocs {

    private final AuthorService authorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO create(@Valid @RequestBody AuthorDTO authorDTO) {
        return authorService.create(authorDTO);
    }

    @GetMapping("/{id}")
    public AuthorDTO findById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @GetMapping
    public List<AuthorDTO> findAll() {
        return authorService.findAll();
    }
}
