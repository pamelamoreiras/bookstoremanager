package com.pamelamoreiras.bookstoremanager.publishers.service;

import com.pamelamoreiras.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.pamelamoreiras.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.publishers.mapper.PublisherMapper;
import com.pamelamoreiras.bookstoremanager.publishers.repository.PublisherRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
    }

    @Test
    void whenNewPublisherIsInformedThenShouldBeCreated() {
        final var expectedPublisherToCreateDTO = publisherDTOBuilder.buildPublisherDTO();
        final var expectedPublisherCreated = publisherMapper.toModel(expectedPublisherToCreateDTO);

        when(publisherRepository.findByNameOrCode(expectedPublisherCreated.getName(),expectedPublisherCreated.getCode()))
                .thenReturn(Optional.empty());
        when(publisherRepository.save(expectedPublisherCreated)).thenReturn(expectedPublisherCreated);

        final var createdPublisherDTO = publisherService.create(expectedPublisherToCreateDTO);

        assertThat(createdPublisherDTO, is(equalTo(expectedPublisherToCreateDTO)));
    }

    @Test
    void whenExistingPublisherIsInformedThenAnExceptionShouldBeThrown() {
        final var expectedPublisherToCreateDTO = publisherDTOBuilder.buildPublisherDTO();
        final var expectedPublisherDuplicated = publisherMapper.toModel(expectedPublisherToCreateDTO);

        final var name = expectedPublisherDuplicated.getName();
        final var code = expectedPublisherDuplicated.getCode();

        when(publisherRepository.findByNameOrCode(name, code))
                .thenReturn(Optional.of(expectedPublisherDuplicated));

        Assertions.assertThrows(PublisherAlreadyExistsException.class,
                () ->  publisherService.create(expectedPublisherToCreateDTO));
    }
}
