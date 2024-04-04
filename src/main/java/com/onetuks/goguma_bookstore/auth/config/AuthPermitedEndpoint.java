package com.onetuks.goguma_bookstore.auth.config;

public class AuthPermitedEndpoint {

  private AuthPermitedEndpoint() {}

  protected static final String[] ENDPOINTS =
      new String[] {
        "/", "/auth/kakao", "/auth/google", "/auth/naver", "/error", "/docs/**", "/actuator/**"
      };
}
