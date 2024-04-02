package com.onetuks.goguma_bookstore.auth.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.onetuks.goguma_bookstore.auth.exception.SecurityExceptionHandlerFilter;
import com.onetuks.goguma_bookstore.auth.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CorsConfig corsConfig;
  private final SecurityExceptionHandlerFilter securityExceptionHandlerFilter;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(
      CorsConfig corsConfig,
      SecurityExceptionHandlerFilter securityExceptionHandlerFilter,
      JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.corsConfig = corsConfig;
    this.securityExceptionHandlerFilter = securityExceptionHandlerFilter;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            request ->
                request
                    .requestMatchers(AuthPermitedEndpoint.ENDPOINTS)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .cors(cors -> corsConfig.corsConfigurationSource())
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(securityExceptionHandlerFilter, JwtAuthenticationFilter.class)
        .build();
  }
}
