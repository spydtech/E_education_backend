// package com.Eonline.Education.Configuration;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.http.HttpMethod;

// import java.util.List;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity(prePostEnabled = true)
// public class AppConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenValidator jwtTokenValidator) throws Exception {
//         http
//                 .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                 .csrf(csrf -> csrf.disable())
//                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                         .requestMatchers("/actuator/health").permitAll()               
//                         .requestMatchers(
//                                 "/", "/api/*", "/auth/**", "/api/auth/**", "/trainee/**", "/trainee/profile",
//                                 "/oauth2/**", "/login/oauth2/**", "/auth/google", "/error"
//                         ).permitAll()
//                         .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                         .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
//                         .requestMatchers("/api/users/profile").authenticated()
//                         .anyRequest().authenticated()
//                 )
//                 .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class);

//         return http.build();
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration cfg = new CorsConfiguration();
//         cfg.setAllowedOriginPatterns(List.of(
//                 "http://localhost",
//                 "http://localhost:5173",
//                 "http://localhost:5174",
//                 "http://localhost:8082",
//                 "http://3.6.36.172:5173",
//                 "http://3.6.36.172:5174",
//                 "http://3.6.36.172:5175",
//                 "http://3.6.36.172:3000",
//                 "https://*.e-education.in",
//                 "https://www.e-education.in",
//                 "https://api.e-education.in",
//                 "https://accounts.google.com"
//         ));

//         cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         cfg.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
//         cfg.setExposedHeaders(List.of("Authorization"));
//         cfg.setAllowCredentials(true);
//         cfg.setMaxAge(3600L);

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", cfg);
//         return source;
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     // Register JwtTokenValidator as a Spring-managed bean
//     @Bean
//     public JwtTokenValidator jwtTokenValidator() {
//         return new JwtTokenValidator();
//     }
// }





//
//
//package com.Eonline.Education.Configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.http.HttpMethod;
//
//import java.util.List;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class AppConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenValidator jwtTokenValidator) throws Exception {
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        .requestMatchers("/health", "/actuator/health", "/error").permitAll()
//                        .requestMatchers("/", "/api/*", "/auth/**", "/auth/login", "/auth/signup", "/auth/register", "/api/auth/**", "/oauth2/**", "/login/oauth2/**", "/auth/google").permitAll()
//                        .requestMatchers("/trainee/**").permitAll()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers("/api/users/profile").authenticated()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class);
//                // .addFilterAfter(jwtTokenValidator(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration cfg = new CorsConfiguration();
//        cfg.setAllowedOriginPatterns(List.of(
//                "http://localhost",
//                "http://localhost:5173",
//                "http://localhost:5174",
//                "http://localhost:8082",
//                "http://82.29.161.78:5173",
//                "http://82.29.161.78:5174",
//                "http://82.29.161.78:5175",
//                "http://82.29.161.78:3000",
//                "https://*.e-education.in",
//                "https://www.e-education.in",
//                "https://e-education.in",
//                "https://api.e-education.in",
//                "https://accounts.google.com"
//        ));
//        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        cfg.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
//        cfg.setExposedHeaders(List.of("Authorization"));
//        cfg.setAllowCredentials(true);
//        cfg.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", cfg);
//        return source;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JwtTokenValidator jwtTokenValidator() {
//        return new JwtTokenValidator();
//    }
//}
package com.Eonline.Education.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class AppConfig {

    // You need UserDetailsService to configure authentication
    private final UserDetailsService userDetailsService;
    private final JwtTokenValidator jwtTokenValidator;

    // Constructor injection
    public AppConfig(UserDetailsService userDetailsService, JwtTokenValidator jwtTokenValidator) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/health", "/actuator/health", "/error").permitAll()
                        .requestMatchers("/", "/api/*", "/auth/**", "/auth/login", "/auth/signup",
                                "/auth/register", "/api/auth/**", "/oauth2/**", "/login/oauth2/**",
                                "/auth/google").permitAll()
                        .requestMatchers("/trainee/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/profile").authenticated()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider()) // Add authentication provider
                .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class);

        return http.build();
    }

    // 🔴 CRITICAL: Add this AuthenticationProvider bean
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 🔴 CRITICAL: Add this AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of(
                "http://localhost",
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:8082",
                "http://82.29.161.78:5173",
                "http://82.29.161.78:5174",
                "http://82.29.161.78:5175",
                "http://82.29.161.78:3000",
                "https://*.e-education.in",
                "https://www.e-education.in",
                "https://e-education.in",
                "https://api.e-education.in",
                "https://accounts.google.com"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Note: Remove this if JwtTokenValidator is already a @Component
    // @Bean
    // public JwtTokenValidator jwtTokenValidator() {
    //     return new JwtTokenValidator();
    // }
}