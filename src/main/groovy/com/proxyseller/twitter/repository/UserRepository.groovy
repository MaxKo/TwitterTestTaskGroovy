package com.proxyseller.twitter.repository

import com.proxyseller.twitter.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username)
}
