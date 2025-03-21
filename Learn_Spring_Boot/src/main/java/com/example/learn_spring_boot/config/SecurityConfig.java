package com.example.learn_spring_boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // ✅ Tắt CSRF
//                .cors(cors -> cors.disable()) // ✅ Tắt CORS trong HttpSecurity, dùng CorsFilter thay thế
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ Không dùng session
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // ✅ Cho phép tất cả API truy cập
//                );
//
//        return http.build();
//    }
//
//    // ✅ Cấu hình CORS đúng cách bằng CorsFilter()
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(List.of("http://localhost:4200")); // 🔹 Chỉ định frontend Angular
//        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}
