package com.onetuks.modulereader.auth.controller.dto;

import com.onetuks.modulereader.auth.service.dto.LoginResult;

public record LoginResponse(String appToken, boolean isNewMember, long loginId, String name) {

  public static LoginResponse from(LoginResult loginResult) {
    return new LoginResponse(
        loginResult.accessToken(),
        loginResult.isNewMember(),
        loginResult.loginId(),
        loginResult.name());
  }
}
