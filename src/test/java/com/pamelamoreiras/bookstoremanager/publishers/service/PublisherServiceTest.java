package com.pamelamoreiras.bookstoremanager.publishers.service;

import com.pamelamoreiras.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.pamelamoreiras.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.pamelamoreiras.bookstoremanager.publishers.mapper.PublisherMapper;
import com.pamelamoreiras.bookstoremanager.publishers.repository.PublisherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

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

    @Test
    void whenValidIdIsGivenThenPublisherShouldBeReturned() {
        final var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        final var expectedPublisherFound = publisherMapper.toModel(expectedPublisherFoundDTO);
        final var expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        when(publisherRepository.findById(expectedPublisherFoundId)).thenReturn(Optional.of(expectedPublisherFound));

        final var foundPublisherDTO = publisherService.findById(expectedPublisherFoundId);

        assertThat(foundPublisherDTO, is(equalTo(foundPublisherDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        final var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        final var expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        when(publisherRepository.findById(expectedPublisherFoundId)).thenReturn(Optional.empty());

        Assertions.assertThrows(PublisherNotFoundException.class, () -> publisherService.findById(expectedPublisherFoundId));
    }
}
