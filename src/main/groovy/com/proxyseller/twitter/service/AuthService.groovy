package com.proxyseller.twitter.service

import com.proxyseller.twitter.api.ApiException
import com.proxyseller.twitter.api.dto.AuthResponse
import com.proxyseller.twitter.api.dto.LoginRequest
import com.proxyseller.twitter.api.dto.RegisterRequest
import com.proxyseller.twitter.domain.Session
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.repository.SessionRepository
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService {

    private final UserRepository userRepository
    private final SessionRepository sessionRepository
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder()

    AuthService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository
        this.sessionRepository = sessionRepository
    }

    AuthResponse register(RegisterRequest req) {
        userRepository.findByUsername(req.username)
                .ifPresent { throw ApiException.badRequest("Username already taken") }

        User user = new User(
                username: req.username,
                passwordHash: encoder.encode(req.password),
                displayName: req.displayName ?: req.username
        )
        userRepository.save(user)

        return login(new LoginRequest(username: req.username, password: req.password))
    }

    AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.username)
                .orElseThrow { ApiException.unauthorized("Invalid credentials") }

        if (!encoder.matches(req.password, user.passwordHash)) {
            throw ApiException.unauthorized("Invalid credentials")
        }

        String token = UUID.randomUUID().toString()
        sessionRepository.save(new Session(token: token, userId: user.id))

        return new AuthResponse(token: token, userId: user.id)
    }

    void logout(String token) {
        if (token) {
            sessionRepository.deleteById(token)
        }
    }
}
