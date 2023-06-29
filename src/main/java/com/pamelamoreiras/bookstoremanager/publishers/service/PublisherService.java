package com.pamelamoreiras.bookstoremanager.publishers.service;

import com.pamelamoreiras.bookstoremanager.publishers.mapper.PublisherMapper;
import com.pamelamoreiras.bookstoremanager.publishers.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private final PublisherRepository publisherRepository;


}
