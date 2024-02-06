package com.sumerge.auth.api;

import com.sumerge.auth.api.DTO.AuthenticationRequest;
import com.sumerge.auth.api.DTO.AuthenticationResponse;
import com.sumerge.auth.api.DTO.RegisterRequest;
import com.sumerge.auth.config.*;
import com.sumerge.auth.user.*;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        User user = User.builder().email(request.getEmail()).build();
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");


        AuthenticationResponse response = authenticationService.register(request);


        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void login() {

        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "securePassword123");
        User user = User.builder().email(request.getEmail()).build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");


        AuthenticationResponse response = authenticationService.login(request);


        assertNotNull(response);
        assertEquals("mockedToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }
}
