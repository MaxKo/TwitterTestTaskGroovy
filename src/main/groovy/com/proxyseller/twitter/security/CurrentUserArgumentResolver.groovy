package com.proxyseller.twitter.security

import com.proxyseller.twitter.api.ApiException
import com.proxyseller.twitter.domain.Session
import com.proxyseller.twitter.domain.User
import com.proxyseller.twitter.repository.SessionRepository
import com.proxyseller.twitter.repository.UserRepository
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.bind.support.WebDataBinderFactory

@Component
class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository
    private final UserRepository userRepository

    CurrentUserArgumentResolver(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository
        this.userRepository = userRepository
    }

    @Override
    boolean supportsParameter(MethodParameter parameter) {
        parameter.hasParameterAnnotation(CurrentUser) && parameter.parameterType == User
    }

    @Override
    Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                           NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String token = webRequest.getHeader("X-Auth-Token")
        if (!token) {
            throw ApiException.unauthorized("Missing X-Auth-Token header")
        }
        Session session = sessionRepository.findById(token).orElseThrow {
            ApiException.unauthorized("Invalid session token")
        }
        return userRepository.findById(session.userId).orElseThrow {
            ApiException.unauthorized("User not found")
        }
    }
}
