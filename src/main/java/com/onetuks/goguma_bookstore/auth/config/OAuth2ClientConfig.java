package com.onetuks.goguma_bookstore.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class OAuth2ClientConfig {

  private final KakaoClientConfig kakaoClientConfig;
  private final GoogleClientConfig googleClientConfig;

  public OAuth2ClientConfig(
      KakaoClientConfig kakaoClientConfig, GoogleClientConfig googleClientConfig) {
    this.kakaoClientConfig = kakaoClientConfig;
    this.googleClientConfig = googleClientConfig;
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    return new InMemoryClientRegistrationRepository(
        kakaoClientConfig.kakaoClientRegistration(), googleClientConfig.googleClientRegistration());
  }
}
