package com.pamelamoreiras.bookstoremanager.users.controller;

import com.pamelamoreiras.bookstoremanager.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs{

    private final UserService userService;
}
