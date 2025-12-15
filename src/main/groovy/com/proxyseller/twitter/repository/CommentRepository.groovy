package com.proxyseller.twitter.repository

import com.proxyseller.twitter.domain.Comment
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByPostIdOrderByCreatedAtAsc(String postId)
}
