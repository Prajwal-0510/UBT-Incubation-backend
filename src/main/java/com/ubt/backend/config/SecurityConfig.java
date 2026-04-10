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
 * SecurityConfig — FIXED
 *
 * KEY FIX: Spring Security evaluates rules top-to-bottom, first match wins.
 * Admin GET routes (/contact/inquiries, /admin/**) MUST be declared
 * BEFORE the public GET /** wildcard, otherwise they get swallowed by it
 * and @PreAuthorize returns 403 even with a valid token.
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

                        // ── 1. Preflight — always open ──────────────────────────────
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ── 2. ADMIN GET routes — MUST be before GET /** wildcard ───
                        // If these come after GET /**, Spring matches that first and
                        // never reaches these rules → @PreAuthorize throws 403.
                        .requestMatchers(HttpMethod.GET, "/contact/inquiries").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contact/inquiries/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")

                        // ── 3. All other GET requests are public ────────────────────
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // ── 4. Public write endpoints ───────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/contact").permitAll()

                        // ── 5. Health check ─────────────────────────────────────────
                        .requestMatchers("/actuator/**").permitAll()

                        // ── 6. Admin writes ─────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST,   "/upload/image").hasRole("ADMIN")
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
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
