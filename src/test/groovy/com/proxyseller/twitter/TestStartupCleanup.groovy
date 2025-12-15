package com.proxyseller.twitter;

import com.proxyseller.twitter.repository.CommentRepository;
import com.proxyseller.twitter.repository.PostRepository;
import com.proxyseller.twitter.repository.SessionRepository;
import com.proxyseller.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class TestStartupCleanup {

    @Autowired
    UserRepository users
    @Autowired
    PostRepository posts
    @Autowired
    CommentRepository comments
    @Autowired
    SessionRepository sessions

    @EventListener(ApplicationReadyEvent.class)
    void cleanup() {
        users.deleteAll()
        posts.deleteAll()
        comments.deleteAll()
        sessions.deleteAll()
    }
}