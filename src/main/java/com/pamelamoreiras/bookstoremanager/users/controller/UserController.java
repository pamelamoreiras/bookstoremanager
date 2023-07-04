package com.pamelamoreiras.bookstoremanager.users.controller;

import com.pamelamoreiras.bookstoremanager.users.dto.MessageDTO;
import com.pamelamoreiras.bookstoremanager.users.dto.UserDTO;
import com.pamelamoreiras.bookstoremanager.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO create(@Valid @RequestBody UserDTO userToCreateDTO) {
        return userService.create(userToCreateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    public MessageDTO update(@PathVariable Long id, @RequestBody @Valid UserDTO userToUpdateDTO) {
        return userService.update(id, userToUpdateDTO);
    }
}
