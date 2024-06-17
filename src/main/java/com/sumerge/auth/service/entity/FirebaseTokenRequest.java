package com.sumerge.auth.service.entity;


import com.sumerge.auth.repository.entity.UserDocument;
import lombok.Getter;

@Getter
public class FirebaseTokenRequest {

        private Boolean isNewUser;

        private String signInMethod;

        private String password;

        private String firebaseToken;

        private String phoneNumber;


}
