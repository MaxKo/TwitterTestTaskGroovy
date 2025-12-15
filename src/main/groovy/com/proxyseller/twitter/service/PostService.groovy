package com.proxyseller.twitter.service

import com.proxyseller.twitter.api.ApiException
import com.proxyseller.twitter.api.dto.*
import com.proxyseller.twitter.domain.Comment
import com.proxyseller.twitter.domain.Post
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.repository.CommentRepository
import com.proxyseller.twitter.repository.PostRepository
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PostService {

    private final PostRepository postRepository
    private final CommentRepository commentRepository
    private final UserRepository userRepository

    PostService(PostRepository postRepository,
                CommentRepository commentRepository,
                UserRepository userRepository) {
        this.postRepository = postRepository
        this.commentRepository = commentRepository
        this.userRepository = userRepository
    }

    PostResponse toResponse(Post post, User currentUser, boolean includeComments = true) {
        User author = userRepository.findById(post.authorId).orElse(null)
        List<CommentResponse> comments = []
        if (includeComments) {
            comments = commentRepository.findAllByPostIdOrderByCreatedAtAsc(post.id).collect { toCommentResponse(it) }
        }
        new PostResponse(
                id: post.id,
                authorId: post.authorId,
                authorUsername: author?.username,
                content: post.content,
                createdAt: post.createdAt,
                updatedAt: post.updatedAt,
                likesCount: post.likes.size(),
                likedByCurrentUser: currentUser != null && post.likes.contains(currentUser.id),
                comments: comments
        )
    }

    CommentResponse toCommentResponse(Comment c) {
        User author = userRepository.findById(c.authorId).orElse(null)
        new CommentResponse(
                id: c.id,
                postId: c.postId,
                authorId: c.authorId,
                authorUsername: author?.username,
                content: c.content,
                createdAt: c.createdAt
        )
    }

    PostResponse create(User current, CreatePostRequest req) {
        Post post = new Post(authorId: current.id, content: req.content)
        postRepository.save(post)
        toResponse(post, current)
    }

    PostResponse update(User current, String postId, UpdatePostRequest req) {
        Post post = postRepository.findById(postId).orElseThrow {
            ApiException.notFound("Post not found")
        }
        if (post.authorId != current.id) {
            throw ApiException.forbidden("You can edit only your own posts")
        }
        post.content = req.content
        post.updatedAt = java.time.Instant.now()
        postRepository.save(post)
        toResponse(post, current)
    }

    void delete(User current, String postId) {
        Post post = postRepository.findById(postId).orElseThrow {
            ApiException.notFound("Post not found")
        }
        if (post.authorId != current.id) {
            throw ApiException.forbidden("You can delete only your own posts")
        }
        postRepository.delete(post)
    }

    PostResponse toggleLike(User current, String postId) {
        Post post = postRepository.findById(postId).orElseThrow {
            ApiException.notFound("Post not found")
        }
        if (post.likes.contains(current.id)) {
            post.likes.remove(current.id)
        } else {
            post.likes.add(current.id)
        }
        postRepository.save(post)
        toResponse(post, current)
    }

    CommentResponse addComment(User current, String postId, CommentRequest req) {
        Post post = postRepository.findById(postId).orElseThrow {
            ApiException.notFound("Post not found")
        }
        Comment comment = new Comment(
                postId: post.id,
                authorId: current.id,
                content: req.content
        )
        commentRepository.save(comment)
        toCommentResponse(comment)
    }

    List<CommentResponse> getComments(String postId) {
        commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId).collect { toCommentResponse(it) }
    }

    List<PostResponse> getOwnFeed(User current) {
        List<String> authors = [] + current.following + [current.id]
        postRepository.findAllByAuthorIdInOrderByCreatedAtDesc(authors)
                .collect { toResponse(it, current) }
    }

    List<PostResponse> getUserFeed(User current, String userId) {
        List<Post> posts = postRepository.findAllByAuthorIdOrderByCreatedAtDesc(userId)
        posts.collect { toResponse(it, current) }
    }
}
