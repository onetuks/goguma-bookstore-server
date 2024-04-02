package com.onetuks.goguma_bookstore.auth.controller.dto;

import com.onetuks.happyparkingserver.auth.service.dto.LoginResult;

public record LoginResponse(String appToken, boolean isNewMember, long memberId, String name) {

  public static LoginResponse from(LoginResult loginResult) {
    return new LoginResponse(
        loginResult.accessToken(),
        loginResult.isNewMember(),
        loginResult.memberId(),
        loginResult.name());
  }
}
