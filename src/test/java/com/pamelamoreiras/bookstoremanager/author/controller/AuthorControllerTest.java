package com.pamelamoreiras.bookstoremanager.author.controller;

import com.pamelamoreiras.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.pamelamoreiras.bookstoremanager.author.service.AuthorService;
import com.pamelamoreiras.bookstoremanager.utils.JsonConversionUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    public static final String AUTHOR_API_URL_PATH = "http://localhost:8080/api/v1/authors";
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldBeReturned() throws Exception {

        final var expectedAuthorToCreteDTO = authorDTOBuilder.buildAuthorDTO();

        when(authorService.create(expectedAuthorToCreteDTO))
                .thenReturn(expectedAuthorToCreteDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedAuthorToCreteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Is.is(expectedAuthorToCreteDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", Is.is(expectedAuthorToCreteDTO.getName())))
                .andExpect(jsonPath("$.age", Is.is(expectedAuthorToCreteDTO.getAge())));
    }

    @Test
    void whenPOSTIsCalledWithOutRequiredFieldThenBadRequestStatusShouldBeInformed() throws Exception {

        final var expectedAuthorToCreteDTO = authorDTOBuilder.buildAuthorDTO();
        expectedAuthorToCreteDTO.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedAuthorToCreteDTO)))
                .andExpect(status().isBadRequest());

    }
}
