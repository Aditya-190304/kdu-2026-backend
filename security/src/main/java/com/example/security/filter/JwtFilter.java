package com.example.security.filter;

import com.example.security.model.User;
import com.example.security.service.UserService;
import com.example.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        final String tokenFromHeader;
        final String extractedUsername;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenFromHeader = authHeader.substring(7);
            extractedUsername = jwtUtil.extractUsername(tokenFromHeader);
        } else {
            tokenFromHeader = null;
            extractedUsername = null;
        }

        if (extractedUsername != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from in-memory UserService
            User user = userService.getAllUsers().stream()
                    .filter(u -> u.getUserName().equals(extractedUsername))
                    .findFirst()
                    .orElse(null);

            if (user != null && jwtUtil.validateToken(tokenFromHeader, user)) {

                // Build UserDetails manually
                var authorities = user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(extractedUsername, null, authorities);

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        chain.doFilter(request, response);
    }
}
