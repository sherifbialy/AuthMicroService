package com.sumerge.auth.service.control;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.sumerge.auth.common.exceptions.AuthException;
import com.sumerge.auth.service.entity.AuthenticationRequest;
import com.sumerge.auth.service.entity.AuthenticationResponse;
import com.sumerge.auth.service.entity.RegisterRequest;
import com.sumerge.auth.security.JwtService;
import com.sumerge.auth.repository.entity.Role;
import com.sumerge.auth.repository.entity.UserDocument;
import com.sumerge.auth.repository.boundary.UserRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sumerge.auth.common.Utilities.getModelMapper;
import static com.sumerge.auth.common.Utilities.getObjectMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthenticationResponse validateAndReturnToken(String token, String phoneNumber, String password) throws FirebaseAuthException, AuthException {
        FirebaseToken decodedToken = firebaseService.verifyIdToken(token);
        String email = decodedToken.getEmail();
        String firebaseId = decodedToken.getUid();
        boolean emailVerified = decodedToken.isEmailVerified();

        UserDocument userDocument;
        Optional<UserDocument> optionalUserDocument = userRepository.findByEmail(email);

        if (optionalUserDocument.isPresent()) {
            userDocument = optionalUserDocument.get();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDocument.getEmail(), password));

        } else {
            String[] nameParts = decodedToken.getName().split(" ");
            String firstName = nameParts[0];
            String lastName = decodedToken.getName().substring(firstName.length()).trim();

            userDocument = UserDocument.builder()
                    .lastName(lastName)
                    .firstName(firstName)
                    .emailVerified(emailVerified)
                    .role(Role.USER)
                    .firebaseId(firebaseId)
                    .phoneNumber(phoneNumber)
                    .build();

            userDocument = userRepository.save(userDocument);
        }
        if (userDocument.isEmailVerified()) {
            String jwt = jwtService.generateToken(userDocument);
            AuthenticationResponse authenticationResponse = getModelMapper().map(userDocument, AuthenticationResponse.class);
            authenticationResponse.setToken(jwt);

            return authenticationResponse;
        } else {
            throw new AuthException("Please verify your email");
        }


    }

    public AuthenticationResponse login(AuthenticationRequest request) {

      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );

      var user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
      if(!user.isEmailVerified()){
          throw new RuntimeException("Please verify your email");
      }
        var token=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();


    }


}
