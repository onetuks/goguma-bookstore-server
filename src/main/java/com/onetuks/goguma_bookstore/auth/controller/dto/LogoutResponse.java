package com.onetuks.goguma_bookstore.auth.controller.dto;

import com.onetuks.goguma_bookstore.auth.service.dto.LogoutResult;

public record LogoutResponse(boolean isLogout) {

  public static LogoutResponse from(LogoutResult logoutResult) {
    return new LogoutResponse(logoutResult.islogout());
  }
}
