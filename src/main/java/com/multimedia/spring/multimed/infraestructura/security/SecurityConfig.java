package com.multimedia.spring.multimed.infraestructura.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/uploads/**", "/ping", "/test-imagen", "/catalogo/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Preflight (CORS) siempre permitido
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        // 2. Rutas de Auth y Archivos
                        .requestMatchers("/auth/**", "/uploads/**", "/test-imagen", "/error").permitAll()
                        // 3. Catálogo y consultas públicas
                        .requestMatchers("/catalogo/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/productos/**", "/promociones/**").permitAll()
                        // 4. Pedidos: Creación pública, Gestión restringida
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/pedidos").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/pedidos/*/estado").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_BOT", "ROLE_CAJERO")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/pedidos/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_BOT", "ROLE_CAJERO")
                        // 5. Usuarios Públicos
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/usuarios").permitAll()
                        // 6. Análisis y Estimación
                        .requestMatchers("/api/analisis/**").authenticated()
                        // 7. Resto de productos y promociones (DELETE, PUT, POST) -> Requieren ADMIN
                        .requestMatchers("/productos/**", "/promociones/**").hasAuthority("ROLE_ADMINISTRADOR")
                        // 8. Cualquier otra cosa autenticada
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            String email = authentication.getName();
            String password = authentication.getCredentials().toString();

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (!passwordEncoder().matches(password, userDetails.getPassword())) {
                throw new org.springframework.security.authentication.BadCredentialsException("Credenciales inválidas");
            }

            return new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Permitir cualquier origen para compatibilidad con Docker Desktop y Túneles (Serveo)
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}