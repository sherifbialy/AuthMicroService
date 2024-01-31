package com.sumerge.auth.api;

import com.sumerge.auth.config.JwtService;
import com.sumerge.auth.user.Role;
import com.sumerge.auth.user.User;
import com.sumerge.auth.user.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) {
        var user= User.builder().
                firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        this.userRepository.save(user);
        var token=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();


    }

    public AuthenticationResponse login(AuthenticationRequest request) {

      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
      var user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        var token=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();

    }

}
