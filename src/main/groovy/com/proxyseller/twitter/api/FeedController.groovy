package com.proxyseller.twitter.api

import com.proxyseller.twitter.api.dto.PostResponse
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.security.CurrentUser
import com.proxyseller.twitter.service.PostService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feed")
class FeedController {

    private final PostService postService

    FeedController(PostService postService) {
        this.postService = postService
    }

    @GetMapping("/me")
    List<PostResponse> myFeed(@CurrentUser User current) {
        postService.getOwnFeed(current)
    }

    @GetMapping("/{userId}")
    List<PostResponse> userFeed(@CurrentUser User current, @PathVariable String userId) {
        postService.getUserFeed(current, userId)
    }
}
