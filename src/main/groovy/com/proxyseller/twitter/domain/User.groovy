package com.proxyseller.twitter.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
class User {
    @Id
    String id
    String username
    String passwordHash
    String displayName
    String bio

    Set<String> followers = [] as Set
    Set<String> following = [] as Set
}
