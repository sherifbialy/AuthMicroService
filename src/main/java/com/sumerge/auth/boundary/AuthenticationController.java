package com.sumerge.auth.boundary;


import com.sumerge.auth.control.AuthenticationService;
import com.sumerge.auth.entity.AuthenticationRequest;
import com.sumerge.auth.entity.AuthenticationResponse;
import com.sumerge.auth.entity.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        try {
            return ResponseEntity.ok(authenticationService.register(registerRequest));

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest){

            return ResponseEntity.ok(authenticationService.login(authRequest));




    }
    @GetMapping("/validate")
    public void validate(){

        //this method authorises through verifying the token in the jwtFilter,
        // hence only response status code is needed
    }

}
