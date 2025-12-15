package com.proxyseller.twitter.api.dto

import groovy.transform.ToString
import jakarta.validation.constraints.NotBlank

@ToString(includeNames = true, excludes = ['password'])
class RegisterRequest {
    @NotBlank
    String username
    @NotBlank
    String password
    String displayName
}

@ToString(includeNames = true, excludes = ['password'])
class LoginRequest {
    @NotBlank
    String username
    @NotBlank
    String password
}

@ToString(includeNames = true)
class AuthResponse {
    String token
    String userId
}
