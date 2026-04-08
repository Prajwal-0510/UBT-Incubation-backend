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
 * ╔══════════════════════════════════════════════════════════╗
 * ║  FIXED Security Config — PUBLIC GET, ADMIN write-only   ║
 * ║                                                          ║
 * ║  PUBLIC  (no token):  ALL GET endpoints                  ║
 * ║  ADMIN   (JWT token): POST / PUT / PATCH / DELETE        ║
 * ╚══════════════════════════════════════════════════════════╝
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

                // ── ALL GET requests are PUBLIC — no token needed ──────────────
                // This is the KEY FIX: data stays visible after admin logout
                .requestMatchers(HttpMethod.GET, "/**").permitAll()

                // ── Pre-flight CORS ────────────────────────────────────────────
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ── Public write endpoints ─────────────────────────────────────
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/contact").permitAll()

                // ── Actuator ───────────────────────────────────────────────────
                .requestMatchers("/actuator/**").permitAll()

                // ── ADMIN: all write operations require JWT ────────────────────
                .requestMatchers(HttpMethod.POST,   "/gallery/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/gallery").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/gallery/**").hasRole("ADMIN")
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

                .requestMatchers(HttpMethod.POST,   "/campus-visits").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/campus-visits/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/campus-visits/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST,   "/testimonials").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/testimonials/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST,   "/footer").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/footer/**").hasRole("ADMIN")

                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/contact/inquiries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/auth/verify").hasRole("ADMIN")



                    .requestMatchers(HttpMethod.POST, "/upload").permitAll()
                // Any other request → allow (since everything readable is GET)
                .anyRequest().permitAll()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.asList(allowedOriginsRaw.split(","));
        config.setAllowedOriginPatterns(origins);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept",
                "Origin", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"
        ));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
