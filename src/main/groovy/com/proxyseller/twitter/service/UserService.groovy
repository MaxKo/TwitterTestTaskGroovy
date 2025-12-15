package com.proxyseller.twitter.service

import com.proxyseller.twitter.api.ApiException
import com.proxyseller.twitter.api.dto.UpdateUserRequest
import com.proxyseller.twitter.api.dto.UserResponse
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {

    private final UserRepository userRepository
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder()

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    UserResponse toResponse(User user) {
        new UserResponse(
                id: user.id,
                username: user.username,
                displayName: user.displayName,
                bio: user.bio,
                followersCount: user.followers.size(),
                followingCount: user.following.size()
        )
    }

    User getById(String id) {
        userRepository.findById(id).orElseThrow {
            ApiException.notFound("User not found")
        }
    }

    UserResponse update(User current, String id, UpdateUserRequest req) {
        if (current.id != id) {
            throw ApiException.forbidden("You can edit only your own profile")
        }
        current.displayName = req.displayName ?: current.displayName
        current.bio = req.bio ?: current.bio
        if (req.password) {
            current.passwordHash = encoder.encode(req.password)
        }
        userRepository.save(current)
        toResponse(current)
    }

    void delete(User current, String id) {
        if (current.id != id) {
            throw ApiException.forbidden("You can delete only your own profile")
        }
        userRepository.deleteById(id)
    }

    UserResponse getProfile(String id) {
        toResponse(getById(id))
    }

    UserResponse follow(User current, String targetId) {
        if (current.id == targetId) {
            throw ApiException.badRequest("You cannot follow yourself")
        }
        User target = getById(targetId)
        current.following.add(target.id)
        target.followers.add(current.id)
        userRepository.save(current)
        userRepository.save(target)
        toResponse(target)
    }

    UserResponse unfollow(User current, String targetId) {
        User target = getById(targetId)
        current.following.remove(target.id)
        target.followers.remove(current.id)
        userRepository.save(current)
        userRepository.save(target)
        toResponse(target)
    }
}
