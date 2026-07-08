package com.ratelimit.Filter;

import com.ratelimit.Service.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final RateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Only enforce rate limiting on /v1/** endpoints
        if (path.startsWith("/api/v1")) {
            String clientId = request.getHeader("X-User-Id");

            if (clientId == null || clientId.isBlank()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Missing X-User-Id header");
                return;
            }

            boolean allowed = rateLimiter.tryConsume(clientId);
            if (!allowed) {
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("Rate limit exceeded for: " + clientId);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}