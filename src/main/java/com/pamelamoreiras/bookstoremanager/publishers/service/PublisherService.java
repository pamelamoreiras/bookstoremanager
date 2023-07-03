package com.pamelamoreiras.bookstoremanager.publishers.service;

import com.pamelamoreiras.bookstoremanager.publishers.dto.PublisherDTO;
import com.pamelamoreiras.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.pamelamoreiras.bookstoremanager.publishers.mapper.PublisherMapper;
import com.pamelamoreiras.bookstoremanager.publishers.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private final PublisherRepository publisherRepository;

    public PublisherDTO create(final PublisherDTO publisherDTO) {

        verifyIfExists(publisherDTO.getName(), publisherDTO.getCode());

        final var publisherToCreate = publisherMapper.toModel(publisherDTO);
        final var createdPublisher = publisherRepository.save(publisherToCreate);

        return publisherMapper.toDTO(createdPublisher);
    }

    public PublisherDTO findById(final Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toDTO)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }

    public void deleteById(final Long id) {
        verifyIfExists(id);
        publisherRepository.deleteById(id);
    }

    public List<PublisherDTO> findAll() {
        return publisherRepository.findAll().stream()
                .map(publisherMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void verifyIfExists(final String name, final String code) {
        final var duplicatedPublisher = publisherRepository.findByNameOrCode(name, code);

        if (duplicatedPublisher.isPresent()) {
            throw new PublisherAlreadyExistsException(name, code);
        }
    }

    private void verifyIfExists(Long id) {
        publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }
}
