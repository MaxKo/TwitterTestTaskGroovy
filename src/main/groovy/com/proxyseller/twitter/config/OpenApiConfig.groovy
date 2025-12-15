package com.proxyseller.twitter.config;

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    static final String AUTH_SCHEME = "XAuthToken"

    @Bean
    OpenAPI openAPI() {
        new OpenAPI()
                .info(new Info()
                        .title("ProxySeller Twitter API")
                        .version("1.0.0")
                )
                .addSecurityItem(
                        new SecurityRequirement().addList(AUTH_SCHEME)
                )
                .components(
                        new Components().addSecuritySchemes(
                                AUTH_SCHEME,
                                new SecurityScheme()
                                        .name("X-Auth-Token")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                )
            )
    }
}