package com.proxyseller.twitter.api

class ApiException extends RuntimeException {
    int status

    ApiException(int status, String message) {
        super(message)
        this.status = status
    }

    static ApiException notFound(String message) {
        new ApiException(404, message)
    }

    static ApiException badRequest(String message) {
        new ApiException(400, message)
    }

    static ApiException unauthorized(String message) {
        new ApiException(401, message)
    }

    static ApiException forbidden(String message) {
        new ApiException(403, message)
    }
}
