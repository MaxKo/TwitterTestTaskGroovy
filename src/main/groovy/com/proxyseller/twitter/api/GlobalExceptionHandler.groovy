package com.proxyseller.twitter.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApiException)
    ResponseEntity<?> handleApiException(ApiException ex) {
        Map body = [message: ex.message]
        ResponseEntity.status(ex.status).body(body)
    }

    @ExceptionHandler(MethodArgumentNotValidException)
    ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.bindingResult.fieldErrors.collect { "${it.field} ${it.defaultMessage}" }.join(", ")
        ResponseEntity.badRequest().body([message: msg])
    }
}
