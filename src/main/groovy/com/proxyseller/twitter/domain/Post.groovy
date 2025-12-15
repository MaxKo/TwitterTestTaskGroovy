package com.proxyseller.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant

@Document("posts")
class Post {
    @Id
    String id
    String authorId
    String content
    Instant createdAt = Instant.now()
    Instant updatedAt = Instant.now()
    Set<String> likes = [] as Set
}
