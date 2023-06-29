package com.pamelamoreiras.bookstoremanager.publishers.controller;

import com.pamelamoreiras.bookstoremanager.publishers.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;
}
