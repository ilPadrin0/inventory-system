package com.example.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final DaoAuthenticationProvider authenticationProvider;
    
    public SecurityConfig(JwtUtil jwtUtil,
	            UserRepository userRepository,
	            DaoAuthenticationProvider authenticationProvider) {
	this.jwtUtil = jwtUtil;
	this.userRepository = userRepository;
	this.authenticationProvider = authenticationProvider;
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(jwtUtil, userRepository);

        http
          .cors().and()
          .csrf().disable()
          .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
          .authorizeHttpRequests(auth -> auth
              // ① 웹소켓 핸드셰이크 허용
              .requestMatchers("/ws/**").permitAll()
              // ② 인증 API
              .requestMatchers("/api/auth/**").permitAll()
              // ③ 상품 조회(GET) – 일반/관리자
              .requestMatchers(HttpMethod.GET, "/api/products/**")
                .hasAnyRole("USER","ADMIN")
              // ④ 상품 변경(POST/PUT/DELETE) – 관리자만
              .requestMatchers("/api/products/**")
                .hasRole("ADMIN")
              // ⑤ 나머지 요청은 인증 필요
              .anyRequest().authenticated()
          )
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
      AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:8081"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }
}
