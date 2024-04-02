package com.onetuks.goguma_bookstore.auth.controller.dto;

import com.onetuks.goguma_bookstore.auth.service.dto.LoginResult;

public record LoginResponse(String appToken, boolean isNewMember, long loginId, String name) {

  public static LoginResponse from(LoginResult loginResult) {
    return new LoginResponse(
        loginResult.accessToken(),
        loginResult.isNewMember(),
        loginResult.loginId(),
        loginResult.name());
  }
}
