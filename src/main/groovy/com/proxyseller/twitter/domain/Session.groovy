package com.proxyseller.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant

@Document("sessions")
class Session {
    @Id
    String token
    String userId
    Instant createdAt = Instant.now()
}
