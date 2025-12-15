package com.proxyseller.twitter.api.dto

import groovy.transform.ToString
import jakarta.validation.constraints.NotBlank

import java.time.Instant

class CreatePostRequest {
    @NotBlank
    String content
}

class UpdatePostRequest {
    @NotBlank
    String content
}

class CommentRequest {
    @NotBlank
    String content
}

ToString
class CommentResponse {
    String id
    String postId
    String authorId
    String authorUsername
    String content
    Instant createdAt
}

@ToString
class PostResponse {
    String id
    String authorId
    String authorUsername
    String content
    Instant createdAt
    Instant updatedAt
    int likesCount
    boolean likedByCurrentUser
    List<CommentResponse> comments
}
