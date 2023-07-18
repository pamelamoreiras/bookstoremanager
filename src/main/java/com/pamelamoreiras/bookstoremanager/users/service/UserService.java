package com.pamelamoreiras.bookstoremanager.users.service;

import com.pamelamoreiras.bookstoremanager.users.dto.MessageDTO;
import com.pamelamoreiras.bookstoremanager.users.dto.UserDTO;
import com.pamelamoreiras.bookstoremanager.users.entity.User;
import com.pamelamoreiras.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.pamelamoreiras.bookstoremanager.users.exception.UserNotFoundException;
import com.pamelamoreiras.bookstoremanager.users.mapper.UserMapper;
import com.pamelamoreiras.bookstoremanager.users.reposirory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.pamelamoreiras.bookstoremanager.users.utils.MessageDTOUtils.creationMessage;
import static com.pamelamoreiras.bookstoremanager.users.utils.MessageDTOUtils.updatedMessage;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public MessageDTO create(final UserDTO userToCreateDTO) {
        verifyIfExists(userToCreateDTO.getEmail(), userToCreateDTO.getUsername());

        final var userToCreate = userMapper.toModel(userToCreateDTO);

        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        final var createdUser = userRepository.save(userToCreate);
        return creationMessage(createdUser);
    }

    public MessageDTO update(final Long id, final UserDTO userToUpdateDTO) {
        final var foundUser = verifyAndGetIfExists(id);
        userToUpdateDTO.setId(foundUser.getId());

        final var userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setCreatedDate(foundUser.getCreatedDate());
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));

        final var updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public void delete(final Long id) {
        verifyAndGetIfExists(id);
        userRepository.deleteById(id);
    }

    private User verifyAndGetIfExists(final Long id) {
         return userRepository.findById(id)
                 .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(final String email, final String username) {
        final var foundUser = userRepository.findByEmailOrUsername(email, username);
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(email, username);
        }
    }

    public User verifyAndGetUserIfExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
