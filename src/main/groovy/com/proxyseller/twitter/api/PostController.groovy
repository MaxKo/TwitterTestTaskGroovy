package com.proxyseller.twitter.api

import com.proxyseller.twitter.api.dto.*
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.security.CurrentUser
import com.proxyseller.twitter.service.PostService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController {

    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping
    PostResponse create(@CurrentUser User current,
                        @Valid @RequestBody CreatePostRequest req) {
        postService.create(current, req)
    }

    @PutMapping("/{id}")
    PostResponse update(@CurrentUser User current,
                        @PathVariable String id,
                        @Valid @RequestBody UpdatePostRequest req) {
        postService.update(current, id, req)
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@CurrentUser User current, @PathVariable String id) {
        postService.delete(current, id)
        ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/like")
    PostResponse toggleLike(@CurrentUser User current, @PathVariable String id) {
        postService.toggleLike(current, id)
    }

    @PostMapping("/{id}/comments")
    CommentResponse comment(@CurrentUser User current,
                            @PathVariable String id,
                            @Valid @RequestBody CommentRequest req) {
        postService.addComment(current, id, req)
    }

    @GetMapping("/{id}/comments")
    List<CommentResponse> getComments(@PathVariable String id) {
        postService.getComments(id)
    }
}
