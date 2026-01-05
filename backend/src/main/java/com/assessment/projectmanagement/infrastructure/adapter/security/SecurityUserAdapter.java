package com.assessment.projectmanagement.infrastructure.adapter.security;

import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.infrastructure.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adapter that implements CurrentUserPort using Spring Security
 */
@Component
public class SecurityUserAdapter implements CurrentUserPort {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityUserAdapter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        // Get the request to extract the token
        org.springframework.web.context.request.ServletRequestAttributes attributes = (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder
                .getRequestAttributes();

        if (attributes != null) {
            jakarta.servlet.http.HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return jwtTokenProvider.extractUserId(token);
            }
        }

        throw new RuntimeException("Unable to extract user ID from token");
    }
}
