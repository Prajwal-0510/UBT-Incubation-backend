package com.ubt.backend.config;

import com.ubt.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * FIXED SecurityConfig
 * - ALL GET requests = public (no token needed)
 * - ALL OPTIONS (preflight) = permitted (fixes CORS error in browser)
 * - POST/PUT/DELETE = admin JWT required
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins}")
    private String allowedOriginsRaw;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // ── CRITICAL: Allow ALL preflight OPTIONS requests ──────────
                        // This is what was causing the CORS error in browser console
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ── ALL GET requests are public — no token needed ───────────
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // ── Public write endpoints ──────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/contact").permitAll()

                        // ── Health check ────────────────────────────────────────────
                        .requestMatchers("/actuator/**").permitAll()

                        // ── ADMIN: writes require JWT ───────────────────────────────
                        .requestMatchers(HttpMethod.POST,   "/gallery").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/gallery/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/projects").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/projects/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/updates").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/updates/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/updates/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/updates/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/alumni").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/alumni/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/alumni/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/footer/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/footer").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/contact/inquiries/**").hasRole("ADMIN")

                        .anyRequest().permitAll()
                );

        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Parse comma-separated origins from env var
        // e.g. CORS_ORIGINS=http://localhost:5173,https://ubt-incubation-frontend-final.vercel.app
        List<String> origins = Arrays.asList(allowedOriginsRaw.split(","));
        config.setAllowedOrigins(origins.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList()));

        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        config.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept", "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));

        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);  // Cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
