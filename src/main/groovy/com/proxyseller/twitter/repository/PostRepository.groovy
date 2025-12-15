package com.proxyseller.twitter.repository

import com.proxyseller.twitter.domain.Post
import org.springframework.data.mongodb.repository.MongoRepository

interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByAuthorIdInOrderByCreatedAtDesc(Collection<String> authorIds)
    List<Post> findAllByAuthorIdOrderByCreatedAtDesc(String authorId)
}
