package com.example.like_api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {


            Optional<String> tokenFromCookie = extractTokenFromCookie(request);

            if (tokenFromCookie.isPresent()) {

                authHeader = "Bearer " + tokenFromCookie.get();

                String finalAuthHeader = authHeader;
                HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if ("Authorization".equalsIgnoreCase(name)) {
                            return finalAuthHeader;
                        }
                        return super.getHeader(name);
                    }
                };
                filterChain.doFilter(requestWrapper, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }


    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .filter(token -> !token.isEmpty())
                    .findFirst();
        }
        return Optional.empty();
    }
}
