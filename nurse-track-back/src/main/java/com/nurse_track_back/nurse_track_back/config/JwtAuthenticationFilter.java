package com.nurse_track_back.nurse_track_back.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
// Disparar siempre que se hace una petition
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7); // 7 pq hay que quitar "Bearer "
        username = jwtService.extractUsername(jwtToken);// Extract username from JWT token;

        // Si hay username y no es ta autenticado, buscarlo
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Si existe recuperar
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Ver si es valido
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                // Si es valido crear un objeto de autenticacion
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Se actualiza autenticacion
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            //
            filterChain.doFilter(request, response);
        }
    }
}
