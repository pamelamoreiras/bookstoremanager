package com.pamelamoreiras.bookstoremanager.books.builder;

import com.pamelamoreiras.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.pamelamoreiras.bookstoremanager.author.dto.AuthorDTO;
import com.pamelamoreiras.bookstoremanager.books.dto.BookResponseDTO;
import com.pamelamoreiras.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.pamelamoreiras.bookstoremanager.publishers.dto.PublisherDTO;
import com.pamelamoreiras.bookstoremanager.users.builder.UserDTOBuilder;
import com.pamelamoreiras.bookstoremanager.users.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookResponseDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Spring Boot";

    @Builder.Default
    private final String isbn = "978-3-16-148410-0";

    @Builder.Default
    private final PublisherDTO publisher = PublisherDTOBuilder.builder().build().buildPublisherDTO();

    @Builder.Default
    private AuthorDTO author = AuthorDTOBuilder.builder().build().buildAuthorDTO();

    @Builder.Default
    private final Integer pages = 200;

    @Builder.Default
    private final Integer chapters = 10;

    private final UserDTO userDTO = UserDTOBuilder.builder().build().buildUserDTO();

    public BookResponseDTO buildResponseBookDTO() {
        return new BookResponseDTO(
                id,
                name,
                isbn,
                pages,
                chapters,
                author,
                publisher
        );
    }
}
