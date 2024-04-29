package com.onetuks.modulereader.auth.controller.dto;

import com.onetuks.modulereader.auth.service.dto.LogoutResult;

public record LogoutResponse(boolean isLogout) {

  public static LogoutResponse from(LogoutResult logoutResult) {
    return new LogoutResponse(logoutResult.islogout());
  }
}
