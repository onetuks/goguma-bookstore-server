package com.onetuks.modulereader.auth.controller.dto;

import com.onetuks.modulereader.auth.service.dto.RefreshResult;

public record RefreshResponse(String accessToken, long loginId) {

  public static RefreshResponse from(RefreshResult refreshResult) {
    return new RefreshResponse(refreshResult.accessToken(), refreshResult.loginId());
  }
}
