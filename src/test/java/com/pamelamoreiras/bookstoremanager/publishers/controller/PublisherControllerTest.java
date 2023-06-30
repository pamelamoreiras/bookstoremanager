package com.pamelamoreiras.bookstoremanager.publishers.controller;

import com.pamelamoreiras.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.pamelamoreiras.bookstoremanager.publishers.service.PublisherService;
import com.pamelamoreiras.bookstoremanager.utils.JsonConversionUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@ExtendWith(MockitoExtension.class)
public class PublisherControllerTest {

    private final static String PUBLISHERS_API_URL_PATH = "/api/v1/publishers";

    private MockMvc mockMvc;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherController publisherController;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusShouldBeInformed() throws Exception{

        final var expectedCreatedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();

        Mockito.when(publisherService.create(expectedCreatedPublisherDTO))
                .thenReturn(expectedCreatedPublisherDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(PUBLISHERS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        Matchers.is(expectedCreatedPublisherDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                        Matchers.is(expectedCreatedPublisherDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code",
                        Matchers.is(expectedCreatedPublisherDTO.getCode())));
    }

    @Test
    void whenPOSTIsCalledWithOutRequiredFieldsThenBadRequestStatusShouldBeInformed() throws Exception{

        final var expectedCreatedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();

        expectedCreatedPublisherDTO.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post(PUBLISHERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedCreatedPublisherDTO)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
