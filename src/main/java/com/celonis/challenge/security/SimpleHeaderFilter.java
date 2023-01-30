package com.celonis.challenge.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleHeaderFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "Celonis-Auth";
    private static final String HEADER_VALUE = "totally_secret";
    private static final String UNAUTHORIZED = "Not authorized";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // OPTIONS should always work
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerValue = request.getHeader(HEADER_NAME);
        if (headerValue == null || !headerValue.equals(HEADER_VALUE)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().append(UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
