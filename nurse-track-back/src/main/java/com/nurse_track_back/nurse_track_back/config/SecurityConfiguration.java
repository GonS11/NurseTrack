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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod; // Importante añadir esto

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public AuthenticationEntryPoint restAuthenticationEntryPoint() {
                return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                                                                authException.getMessage());
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        // Configuración CORS
                        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                        // Deshabilitar CSRF para APIs REST sin estado
                        .csrf(AbstractHttpConfigurer::disable)
                        // Deshabilitar Basic Auth
                        .httpBasic(AbstractHttpConfigurer::disable)
                        // Configuración de autorización de solicitudes
                        .authorizeHttpRequests(auth -> auth
                                // Endpoints que no requieren autenticación
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html")
                                .permitAll()
                                // Permitir todas las solicitudes OPTIONS para cualquier path
                                // Esto es CRUCIAL para las preflight requests de CORS
                                .requestMatchers(HttpMethod.OPTIONS, "/**")
                                .permitAll()
                                // Todas las demás solicitudes requieren autenticación
                                .anyRequest().authenticated())
                        // Configuración de gestión de sesión como stateless (para JWT)
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        // Proveedor de autenticación
                        .authenticationProvider(authenticationProvider)
                        // Manejo de excepciones para autenticación
                        .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint()))
                        // Añadir el filtro JWT antes del filtro de autenticación de usuario/contraseña
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of(
                        "http://localhost:5173",
                        "http://127.0.0.1:5173",
                        "http://localhost"
                ));
                configuration.setAllowedMethods(Arrays.asList(
                        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setExposedHeaders(List.of(
                        "Authorization",
                        "Content-Disposition"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}