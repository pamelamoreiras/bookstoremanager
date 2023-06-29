package com.pamelamoreiras.bookstoremanager.publishers.builder;

import com.pamelamoreiras.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class PublisherDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Alef Chaves";

    @Builder.Default
    private final String code = "ALE1234";

    @Builder.Default
    private final LocalDate foundationDate = LocalDate.of(2022, 6, 9);

    public PublisherDTO buildPublisherDTO() {
        return new PublisherDTO(id,
                name,
                code,
                foundationDate);
    }
}
