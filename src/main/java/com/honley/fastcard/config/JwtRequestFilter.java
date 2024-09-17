package com.honley.fastcard.config;

import com.honley.fastcard.service.UserService;
import com.honley.fastcard.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtils;
    private final UserService userService;

    private static final List<String> PROTECTED_PATHS = List.of(
            "/api/v1/users/.*",
            "/api/v1/users/all",
            "/api/v1/users/update-password-request"
    );

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/v1/users/register",
            "/api/v1/users/authenticate",
            "/api/v1/users/activate/.*",
            "/api/v1/users/update-password"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        String requestURI = request.getRequestURI();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtTokenUtils.getUsername(jwt);
            } catch (ExpiredJwtException e) {
                log.debug("Token lifetime has expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            } catch (SignatureException e) {
                log.debug("Signature is incorrect");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (isProtectedPath(requestURI) && !isExcludedPath(requestURI)) {
                try {
                    boolean isActivated = userService.isUserActivated(username);
                    if (!isActivated) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account is not activated");
                        return;
                    }
                } catch (Exception e) {
                    log.error("Error checking user activation status", e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
                    return;
                }
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtTokenUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isProtectedPath(String requestURI) {
        return PROTECTED_PATHS.stream().anyMatch(requestURI::matches);
    }

    private boolean isExcludedPath(String requestURI) {
        return EXCLUDED_PATHS.stream().anyMatch(requestURI::matches);
    }
}
