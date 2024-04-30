package com.onetuks.moduleauth.config;

public class AuthPermittedEndpoint {

  private AuthPermittedEndpoint() {}

  protected static final String[] ENDPOINTS =
      new String[] {
        "/", "/auth/kakao", "/auth/google", "/auth/naver", "/error", "/docs/**", "/actuator/**"
      };
}
