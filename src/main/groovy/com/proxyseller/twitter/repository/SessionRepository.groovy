package com.proxyseller.twitter.repository

import com.proxyseller.twitter.domain.Session
import org.springframework.data.mongodb.repository.MongoRepository

interface SessionRepository extends MongoRepository<Session, String> {
}
