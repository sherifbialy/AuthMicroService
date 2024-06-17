package com.sumerge.auth.service.boundary;


import com.google.firebase.auth.FirebaseAuthException;
import com.sumerge.auth.common.exceptions.AuthException;
import com.sumerge.auth.repository.boundary.UserRepository;
import com.sumerge.auth.repository.entity.UserDocument;
import com.sumerge.auth.service.control.AuthenticationService;
import com.sumerge.auth.service.control.FirebaseService;
import com.sumerge.auth.service.entity.AuthenticationRequest;
import com.sumerge.auth.service.entity.AuthenticationResponse;
import com.sumerge.auth.service.entity.FirebaseTokenRequest;
import com.sumerge.auth.service.entity.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationResource {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    @GetMapping
    public List<UserDocument> getAll(){
        return userRepository.findAll();
    }


    @PostMapping("/uid/verify_email")
    public ResponseEntity<String> verifyEmail(@PathVariable String uid,@RequestBody Boolean emailVerified) {


            Optional<UserDocument> user = userRepository.findById(UUID.fromString(uid));
            if (user.isEmpty()) {
                return ResponseEntity.status(401).body("User not found");
            }
            else{
                user.get().setEmailVerified(true);
                userRepository.save(user.get());
                return ResponseEntity.ok("Email verified set to true");
            }

    }

    @PostMapping("/uid/fcm_token")
    public ResponseEntity<String> verifyEmail(@PathVariable String uid,@RequestBody String FCMToken) {


        Optional<UserDocument> user = userRepository.findById(UUID.fromString(uid));
        if (user.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid or expired verification link");
        }
        else{
            UserDocument userDocument = user.get();
            userDocument.setFCMToken(FCMToken);
            userDocument.setFCMTimestamp(new Date());
            userRepository.save(user.get());
            return ResponseEntity.ok("Email verified set to true");
        }

    }

    @PostMapping("/firebase")
    public ResponseEntity<AuthenticationResponse> authenticateWithFirebase(@RequestBody FirebaseTokenRequest tokenRequest) {
        String firebaseToken = tokenRequest.getFirebaseToken();
        try {
            AuthenticationResponse jwt= authenticationService.validateAndReturnToken(firebaseToken, tokenRequest.getPhoneNumber(),tokenRequest.getSignInMethod());
            return ResponseEntity.ok( AuthenticationResponse.builder().token(jwt.getToken()).build());
        } catch (FirebaseAuthException | AuthException e) {
            return ResponseEntity.status(400).body(AuthenticationResponse.builder().error(e.getMessage()).build());
        }
    }

    @PostMapping("/uid")
    public ResponseEntity<UserDocument> getUserByUid(@RequestBody String uid){
        {
            try{
                Optional<UserDocument> optionalUserDocument = userRepository.findById(UUID.fromString(uid));
                return optionalUserDocument.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
            }
            catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @PostMapping("firebase/uid")
    public ResponseEntity<UserDocument> getUserByFirebaseUid(@RequestBody String uid){
        {
            try{
                return ResponseEntity.ok(userRepository.findByFirebaseId(uid));
            }
            catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }
    }

    @PostMapping("firebase/convert_id")
    public ResponseEntity<UserDocument> getUserIdByFirebaseId(@RequestBody String firebaseId){
        {
            try{
                UserDocument byFirebaseId = userRepository.findByFirebaseId(firebaseId);
                if(byFirebaseId == null){
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(byFirebaseId);
            }
            catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }
    }


    @GetMapping("/validate")
    public void validate(){

        //this method authorises through verifying the token in the jwtFilter,
        // hence only response status code is needed
    }

}
