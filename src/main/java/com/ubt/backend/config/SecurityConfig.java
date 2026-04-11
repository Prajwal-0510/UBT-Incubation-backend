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
import java.util.stream.Collectors;

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
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 1. Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Public GET routes
                        .requestMatchers(HttpMethod.GET, "/gallery/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/updates/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/alumni/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/testimonials/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/footer/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()

                        // 3. Public POST routes
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/contact").permitAll()

                        // 4. Upload — permit all (JWT checked by @PreAuthorize in other routes)
                        .requestMatchers(HttpMethod.POST, "/upload").permitAll()
                        .requestMatchers(HttpMethod.POST, "/upload/image").permitAll()

                        // 5. Admin-only GET routes (MUST be before anyRequest)
                        .requestMatchers(HttpMethod.GET, "/contact/inquiries/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")

                        // 6. Admin writes
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
                        .requestMatchers(HttpMethod.POST,   "/testimonials").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/testimonials/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/contact/inquiries/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/contact/inquiries/**").hasRole("ADMIN")

                        .anyRequest().permitAll()
                );

        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = Arrays.stream(allowedOriginsRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        config.setAllowedOrigins(origins);
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