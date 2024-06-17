package com.sumerge.auth.service;

import com.sumerge.auth.repository.boundary.UserRepository;
import com.sumerge.auth.repository.entity.UserDocument;
import com.sumerge.auth.security.JwtService;
import com.sumerge.auth.service.entity.AuthenticationRequest;
import com.sumerge.auth.service.entity.AuthenticationResponse;
import com.sumerge.auth.service.entity.RegisterRequest;
import com.sumerge.auth.service.control.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;



    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "securePassword123");
        UserDocument userDocument = UserDocument.builder().email(request.getEmail()).build();
        when(userRepository.save(any(UserDocument.class))).thenReturn(userDocument);
        when(jwtService.generateToken(any(UserDocument.class))).thenReturn("mockedToken");


        AuthenticationResponse response = authenticationService.register(request);


        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        verify(userRepository, times(1)).save(any(UserDocument.class));
        verify(jwtService, times(1)).generateToken(any(UserDocument.class));
    }

    @Test
    void login() {

        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "securePassword123");
        UserDocument userDocument = UserDocument.builder().email(request.getEmail()).build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(userDocument));
        when(jwtService.generateToken(any(UserDocument.class))).thenReturn("mockedToken");


        AuthenticationResponse response = authenticationService.login(request);


        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(UserDocument.class));
    }
}
