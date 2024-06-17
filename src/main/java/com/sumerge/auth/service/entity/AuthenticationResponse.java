package com.sumerge.auth.service.entity;

import com.sumerge.auth.repository.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String error;

    private String email;
    private String firebaseId;
    private String firstName;
    private String lastName;
    private List<UUID> userFavorites;
    private String phoneNumber;
    private String password;
    private Role role;
    private boolean emailVerified;
    private String FCMToken;
    private Date FCMTimestamp;

}