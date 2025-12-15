package com.proxyseller.twitter.api

import com.proxyseller.twitter.api.dto.UpdateUserRequest
import com.proxyseller.twitter.api.dto.UserResponse
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.security.CurrentUser
import com.proxyseller.twitter.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @GetMapping("/{id}")
    UserResponse getUser(@PathVariable String id) {
        userService.getProfile(id)
    }

    @PutMapping("/{id}")
    UserResponse update(@CurrentUser User current,
                        @PathVariable String id,
                        @Valid @RequestBody UpdateUserRequest req) {
        userService.update(current, id, req)
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@CurrentUser User current, @PathVariable String id) {
        userService.delete(current, id)
        ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/follow")
    UserResponse follow(@CurrentUser User current, @PathVariable String id) {
        userService.follow(current, id)
    }

    @DeleteMapping("/{id}/follow")
    UserResponse unfollow(@CurrentUser User current, @PathVariable String id) {
        userService.unfollow(current, id)
    }
}
