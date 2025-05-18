package com.nurse_track_back.nurse_track_back.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  // Activa seguridad web
@EnableMethodSecurity // Permite @PreAuthorize, @RolesAllowed, etc. :contentReference[oaicite:4]{index=4}
@RequiredArgsConstructor
public class SecurityConfiguration
{
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // Define un entry point que responde 401 en vez de redirigir :contentReference[oaicite:5]{index=5}
    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint()
    {
        return (request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                // 1. Desactiva CSRF y HTTP Basic (por defecto Spring Security habilita HTTP Basic) :contentReference[oaicite:6]{index=6}
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 2. Configura las reglas de autorización con la nueva API funcional :contentReference[oaicite:7]{index=7}
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()   // Permite registro/autenticación
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/supervisor/**").hasRole("SUPERVISOR")
                        .requestMatchers("/api/nurse/**").hasRole("NURSE")
                        .anyRequest().authenticated()                  // Resto de rutas requieren token
                )

                // 3. Política sin estado para API REST :contentReference[oaicite:8]{index=8}
                .sessionManagement(session ->
                                           session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Provee el AuthenticationProvider que valida credenciales / JWT :contentReference[oaicite:9]{index=9}
                .authenticationProvider(authenticationProvider)

                // 5. Gestiona respuestas 401 en caso de fallo de autenticación :contentReference[oaicite:10]{index=10}
                .exceptionHandling(ex ->
                                           ex.authenticationEntryPoint(restAuthenticationEntryPoint())
                )

                // 6. Filtra JWT antes que la autenticación de usuario/password :contentReference[oaicite:11]{index=11}
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
