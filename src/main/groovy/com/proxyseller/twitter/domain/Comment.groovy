package com.proxyseller.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.Instant

@Document("comments")
class Comment {
    @Id
    String id
    String postId
    String authorId
    String content
    Instant createdAt = Instant.now()
}
