package com.pamelamoreiras.bookstoremanager.users.service;

import com.pamelamoreiras.bookstoremanager.users.builder.JwtRequestBuilder;
import com.pamelamoreiras.bookstoremanager.users.builder.UserDTOBuilder;
import com.pamelamoreiras.bookstoremanager.users.mapper.UserMapper;
import com.pamelamoreiras.bookstoremanager.users.reposirory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserDTOBuilder userDTOBuilder;

    private JwtRequestBuilder jwtRequestBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
    }

    @Test
    void whenUsernameAndPasswordIsInformedThenATokenShouldBeGenerated() {
        final var jwtRequest = jwtRequestBuilder.buildJwtRequest();
        final var expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        final var expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        final var expectedGeneratedToken = "fakeToken";

        when(userRepository.findByUsername(jwtRequest.getUsername()))
                .thenReturn(Optional.of(expectedFoundUser));
        when(jwtTokenManager.generateToken(any(UserDetails.class)))
                .thenReturn(expectedGeneratedToken);

        final var generatedTokenResponse = authenticationService.createAuthenticationToken(jwtRequest);

        assertThat(generatedTokenResponse.getToken(), is(equalTo(expectedGeneratedToken)));
    }

    @Test
    void whenUsernameIsInformedThenUserShouldBeReturned() {

        final var expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        final var expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        final var expectedUserRole = new SimpleGrantedAuthority("ROLE_" + expectedFoundUserDTO.getRole().getDescription());

        final var expectedUsername = expectedFoundUserDTO.getUsername();

        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.of(expectedFoundUser));

        final var userDetails = authenticationService.loadUserByUsername(expectedUsername);

        assertThat(userDetails.getUsername(), is(equalTo(expectedFoundUser.getUsername())));
        assertThat(userDetails.getPassword(), is(equalTo(expectedFoundUser.getPassword())));
        assertTrue(userDetails.getAuthorities().contains(expectedUserRole));
    }

    @Test
    void whenInvalidUserIdIsInformedThenAnExceptionShouldBeThrown() {
        final var  expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        final var expectedUsername = expectedFoundUserDTO.getUsername();

        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(expectedUsername));
    }
}
