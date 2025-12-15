package com.proxyseller.twitter.api.dto

class UserResponse {
    String id
    String username
    String displayName
    String bio
    int followersCount
    int followingCount
}

class UpdateUserRequest {
    String displayName
    String bio
    String password
}
