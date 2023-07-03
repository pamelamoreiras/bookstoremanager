package com.pamelamoreiras.bookstoremanager.publishers.controller;

import com.pamelamoreiras.bookstoremanager.publishers.dto.PublisherDTO;
import com.pamelamoreiras.bookstoremanager.publishers.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController implements PublisherControllerDocs{

    private final PublisherService publisherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDTO create(@RequestBody @Valid PublisherDTO publisherDTO) {
        return publisherService.create(publisherDTO);
    }

    @GetMapping("/{id}")
    public PublisherDTO findById(@PathVariable Long id) {
        return publisherService.findById(id);
    }

    @GetMapping
    public List<PublisherDTO> findAll() {
        return publisherService.findAll();
    }
}
