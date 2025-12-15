package com.proxyseller.twitter.api

import com.proxyseller.twitter.api.dto.AuthResponse
import com.proxyseller.twitter.api.dto.LoginRequest
import com.proxyseller.twitter.api.dto.RegisterRequest
import com.proxyseller.twitter.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final AuthService authService

    AuthController(AuthService authService) {
        this.authService = authService
    }

    @PostMapping("/register")
    AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req)
    }

    @PostMapping("/login")
    AuthResponse login(@Valid @RequestBody LoginRequest req) {
        authService.login(req)
    }

    @PostMapping("/logout")
    ResponseEntity<?> logout(@RequestHeader("X-Auth-Token") String token) {
        authService.logout(token)
        ResponseEntity.ok().build()
    }
}
