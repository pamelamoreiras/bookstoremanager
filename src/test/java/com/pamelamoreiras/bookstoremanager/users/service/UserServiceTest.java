package com.pamelamoreiras.bookstoremanager.users.service;

import com.pamelamoreiras.bookstoremanager.users.builder.UserDTOBuilder;
import com.pamelamoreiras.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.users.exception.UserNotFoundException;
import com.pamelamoreiras.bookstoremanager.users.mapper.UserMapper;
import com.pamelamoreiras.bookstoremanager.users.reposirory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenNewUserIsInformedThenItShouldBeCreated() {
        final var expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();
        final var expectedCreatedUser = userMapper.toModel(expectedCreatedUserDTO);
        final var expectedCreationMessage = "User pamelamoreiras with ID 1 successfully created";

        final var expectedUserEmail = expectedCreatedUserDTO.getEmail();
        final var expectedUsername = expectedCreatedUserDTO.getUsername();

        when(userRepository.findByEmailOrUsername(expectedUserEmail, expectedUsername)).thenReturn(Optional.empty());
        when(userRepository.save(expectedCreatedUser)).thenReturn(expectedCreatedUser);

        final var creationMessage = userService.create(expectedCreatedUserDTO);

        assertThat(expectedCreationMessage, is(equalTo(creationMessage.getMessage())));
    }

    @Test
    void whenExistingUserIsInformedThenAnExceptionShouldBeThrown() {
        final var expectedDuplicatedUserDTO = userDTOBuilder.buildUserDTO();
        final var expectedDuplicatedUser = userMapper.toModel(expectedDuplicatedUserDTO);

        final var expectedDuplicatedEmail = expectedDuplicatedUserDTO.getEmail();
        final var expectedDuplicatedUsername = expectedDuplicatedUserDTO.getUsername();

        when(userRepository.findByEmailOrUsername(expectedDuplicatedEmail, expectedDuplicatedUsername)).thenReturn(Optional.of(expectedDuplicatedUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(expectedDuplicatedUserDTO));
    }

    @Test
    void whenValidUserIsInformedThenItShouldBeDeleted() {
        final var expectedDeletedUserDTO = userDTOBuilder.buildUserDTO();
        final var expectedDeletedUser = userMapper.toModel(expectedDeletedUserDTO);
        var expectedDeletedUserId = expectedDeletedUserDTO.getId();

        when(userRepository.findById(expectedDeletedUserId)).thenReturn(Optional.of(expectedDeletedUser));
        doNothing().when(userRepository).deleteById(expectedDeletedUserId);

        userService.delete(expectedDeletedUserId);

        verify(userRepository, times(1)).deleteById(expectedDeletedUserId);
    }

    @Test
    void whenInvalidUserIdIsInformedThenAnExceptionShouldBeThrown() {
        final var expectedDeletedUserDTO = userDTOBuilder.buildUserDTO();
        var expectedDeletedUserId = expectedDeletedUserDTO.getId();

        when(userRepository.findById(expectedDeletedUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(expectedDeletedUserId));
    }

    @Test
    void whenExistingUserIsInformedThenItShouldBeUpdated() {
        final var expectedUpdatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedUpdatedUserDTO.setUsername("pamelaUpdate");
        final var expectedUpdatedUser = userMapper.toModel(expectedUpdatedUserDTO);
        String expectedUpdatedMessage = "User pamelaUpdate with ID 1 successfully updated";

        when(userRepository.findById(expectedUpdatedUserDTO.getId())).thenReturn(Optional.of(expectedUpdatedUser));
        when(userRepository.save(expectedUpdatedUser)).thenReturn(expectedUpdatedUser);

        final var successUpdatedMessage = userService.update(expectedUpdatedUserDTO.getId(), expectedUpdatedUserDTO);

        assertThat(successUpdatedMessage.getMessage(), is(equalToObject(expectedUpdatedMessage)));

    }

    @Test
    void whenNotExistingUserIsInformedThenAnExceptionShouldBeThrown() {
        final var expectedUpdatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedUpdatedUserDTO.setUsername("pamelaUpdate");

        when(userRepository.findById(expectedUpdatedUserDTO.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(expectedUpdatedUserDTO.getId(), expectedUpdatedUserDTO));
    }
}
