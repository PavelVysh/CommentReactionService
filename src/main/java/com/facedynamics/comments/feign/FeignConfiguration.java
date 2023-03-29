package com.facedynamics.comments.feign;

import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableFeignClients
public class FeignConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                requestTemplate.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwt.getTokenValue()));
            }
        };
    }

}