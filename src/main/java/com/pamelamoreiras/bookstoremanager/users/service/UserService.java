package com.pamelamoreiras.bookstoremanager.users.service;

import com.pamelamoreiras.bookstoremanager.users.dto.MessageDTO;
import com.pamelamoreiras.bookstoremanager.users.dto.UserDTO;
import com.pamelamoreiras.bookstoremanager.users.entity.User;
import com.pamelamoreiras.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.users.exception.UserNotFoundException;
import com.pamelamoreiras.bookstoremanager.users.mapper.UserMapper;
import com.pamelamoreiras.bookstoremanager.users.reposirory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    public MessageDTO create(final UserDTO userToCreateDTO) {
        verifyIfExists(userToCreateDTO.getEmail(), userToCreateDTO.getUsername());

        final var userToCreate = userMapper.toModel(userToCreateDTO);
        final var createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public void delete(Long id) {
        verifyIfExists(id);
        userRepository.deleteById(id);
    }

    private void verifyIfExists(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(String email, String username) {
        final var foundUser = userRepository.findByEmailOrUsername(email, username);
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(email, username);
        }
    }

    private static MessageDTO creationMessage(final User createdUser) {
        final var createdUserName = createdUser.getUsername();
        final var createdUserId = createdUser.getId();
        final var createdUserMessage = String.format("User %s with ID %s successfully created", createdUserName, createdUserId);

        return MessageDTO.builder()
                .message(createdUserMessage)
                .build();
    }
}
