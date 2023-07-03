package com.pamelamoreiras.bookstoremanager.users.service;

import com.pamelamoreiras.bookstoremanager.users.mapper.UserMapper;
import com.pamelamoreiras.bookstoremanager.users.reposirory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private final UserRepository userRepository;
}
