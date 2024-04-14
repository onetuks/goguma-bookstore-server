package com.onetuks.goguma_bookstore.auth.service.dto;

public record LoginResult(String accessToken, boolean isNewMember, long loginId, String name) {

  public static LoginResult of(String accessToken, boolean isNewMember, Long loginId, String name) {
    return new LoginResult(accessToken, isNewMember, loginId, name);
  }
}
