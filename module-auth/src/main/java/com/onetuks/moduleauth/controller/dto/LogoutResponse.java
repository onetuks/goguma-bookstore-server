package com.onetuks.moduleauth.controller.dto;

import com.onetuks.moduleauth.service.dto.LogoutResult;

public record LogoutResponse(boolean isLogout) {

  public static LogoutResponse from(LogoutResult logoutResult) {
    return new LogoutResponse(logoutResult.islogout());
  }
}
