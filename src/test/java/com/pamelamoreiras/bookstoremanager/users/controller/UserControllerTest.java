package com.pamelamoreiras.bookstoremanager.users.controller;

import com.pamelamoreiras.bookstoremanager.users.builder.UserDTOBuilder;
import com.pamelamoreiras.bookstoremanager.users.dto.MessageDTO;
import com.pamelamoreiras.bookstoremanager.users.service.UserService;
import com.pamelamoreiras.bookstoremanager.utils.JsonConversionUtils;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String USER_API_URL_PATH = "http://localhost:8080/api/v1/users";
    
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTOBuilder userDTOBuilder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldBeReturned() throws Exception{
        final var expectedUserToCreateDTO = userDTOBuilder.buildUserDTO();
        final var expectedCreationMessage = "User pamelamoreiras with ID 1 successfully created";
        final var expectedCreationMessageDTO = MessageDTO.builder()
                .message(expectedCreationMessage)
                .build();

        when(userService.create(expectedUserToCreateDTO)).thenReturn(expectedCreationMessageDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(USER_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedUserToCreateDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedCreationMessage)));
    }

    @Test
    void whenPOSTIsCalledWithOutRequiredFieldThenBadRequestStatusShouldBeReturned() throws Exception{
        final var expectedUserToCreateDTO = userDTOBuilder.buildUserDTO();
        expectedUserToCreateDTO.setUsername(null);

        mockMvc.perform(MockMvcRequestBuilders.post(USER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedUserToCreateDTO)))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void whenDELETEWithValidIdIsCalledThenNoContentShouldBeInformed() throws Exception {
        final var expectedUserToDeleteDTO = userDTOBuilder.buildUserDTO();

        doNothing().when(userService).delete(expectedUserToDeleteDTO.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete(USER_API_URL_PATH + "/" + expectedUserToDeleteDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void whenPUTIsCalledThenOkStatusShouldBeReturned() throws Exception{
        final var expectedUserToUpdateDTO = userDTOBuilder.buildUserDTO();
        expectedUserToUpdateDTO.setUsername("pamelaUpdate");
        final var expectedUpdatedMessage = "User pamelaUpdate with ID 1 successfully updated";
        final var expectedUpdatedMessageDTO = MessageDTO.builder().message(expectedUpdatedMessage).build();
        final var expectedUserToUpdateId = expectedUserToUpdateDTO.getId();

        when(userService.update(expectedUserToUpdateId, expectedUserToUpdateDTO)).thenReturn(expectedUpdatedMessageDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(USER_API_URL_PATH + "/" + expectedUserToUpdateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedUserToUpdateDTO)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedUpdatedMessage)));
    }
}
