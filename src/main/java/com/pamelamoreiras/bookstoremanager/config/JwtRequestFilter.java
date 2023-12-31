package com.pamelamoreiras.bookstoremanager.config;

import com.pamelamoreiras.bookstoremanager.users.service.AuthenticationService;
import com.pamelamoreiras.bookstoremanager.users.service.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {

        var username = "";
        var jwtToken = "";
        var requestTokenHeader = request.getHeader("Authorization");

        if (isTokenPresent(requestTokenHeader)) {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtTokenManager.getUsernameFromToken(jwtToken);
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (isUsernameInContext(username)) {
            addUsernameInContext(request, username, jwtToken);
        }

        chain.doFilter(request, response);
    }

    private static boolean isTokenPresent(final String requestTokenHeader) {
        return requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ");
    }

    private static boolean isUsernameInContext(final String username) {
        return !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void addUsernameInContext(final HttpServletRequest request, final String username, final String jwtToken) {
        final var userDetails = authenticationService.loadUserByUsername(username);

        if (jwtTokenManager.validateToken(jwtToken, userDetails)) {
            var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
