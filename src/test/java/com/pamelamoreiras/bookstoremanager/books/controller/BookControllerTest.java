package com.pamelamoreiras.bookstoremanager.books.controller;

import com.pamelamoreiras.bookstoremanager.books.builder.BookRequestDTOBuilder;
import com.pamelamoreiras.bookstoremanager.books.builder.BookResponseDTOBuilder;
import com.pamelamoreiras.bookstoremanager.books.service.BookService;
import com.pamelamoreiras.bookstoremanager.users.dto.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.pamelamoreiras.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private static final String BOOKS_API_URL_PATH = "/api/v1/books";

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private BookRequestDTOBuilder bookRequestDTOBuilder;

    private BookResponseDTOBuilder bookResponseDTOBuilder;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .addFilters()
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusShouldBeReturned() throws Exception {
        final var expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        final var expectedCreatedBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.create(ArgumentMatchers.any(AuthenticatedUser.class), eq(expectedBookToCreateDTO)))
                .thenReturn(expectedCreatedBookDTO);


        mockMvc.perform(MockMvcRequestBuilders.post(BOOKS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBookToCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedBookDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedBookDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedCreatedBookDTO.getIsbn())));
    }

    @Test
    void whenPOSTIsCalledWithOutRequiredFieldsThenBadRequestStatusShouldBeReturned() throws Exception {
        final var expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        expectedBookToCreateDTO.setIsbn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(BOOKS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBookToCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETWhitValidIdIsCalledThenOkStatusShouldBeInformed() throws Exception {
        final var expectedBookToFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        final var expectedFoundBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.findByIdAndUser(ArgumentMatchers.any(AuthenticatedUser.class), eq(expectedBookToFindDTO.getId())))
                .thenReturn(expectedFoundBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_API_URL_PATH + "/" + expectedBookToFindDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedFoundBookDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedFoundBookDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedFoundBookDTO.getIsbn())));
    }

    @Test
    void whenGETListIsCalledThenStatusOkShouldBeInformed() throws Exception {
        final var expectedFoundBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.findAllByUser(ArgumentMatchers.any(AuthenticatedUser.class)))
                .thenReturn(Collections.singletonList(expectedFoundBookDTO));

        mockMvc.perform(MockMvcRequestBuilders.get(BOOKS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(expectedFoundBookDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedFoundBookDTO.getName())))
                .andExpect(jsonPath("$[0].isbn", is(expectedFoundBookDTO.getIsbn())));
    }

    @Test
    void whenDELETEIsCalledWithValidBookIdThenNoContentOkShouldBeInformed() throws Exception {
        final var expectedBookToDeleteDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        doNothing().when(bookService).deleteByIdAndUser(ArgumentMatchers.any(AuthenticatedUser.class), eq(expectedBookToDeleteDTO.getId()));

        mockMvc.perform(MockMvcRequestBuilders.delete(BOOKS_API_URL_PATH + "/" + expectedBookToDeleteDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPUTIsCalledThenOkStatusShouldBeReturned() throws Exception {
        final var expectedBookToUpdateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        final var expectedUpdatedBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.updateByIdAndUser(
                ArgumentMatchers.any(AuthenticatedUser.class), eq(expectedBookToUpdateDTO.getId()), eq(expectedBookToUpdateDTO)
        )).thenReturn(expectedUpdatedBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(BOOKS_API_URL_PATH + "/" + expectedUpdatedBookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBookToUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookToUpdateDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedBookToUpdateDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedBookToUpdateDTO.getIsbn())));
    }
}
