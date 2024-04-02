package com.onetuks.goguma_bookstore.auth.service.dto;

public record LoginResult(String accessToken, boolean isNewMember, long memberId, String name) {

  public static LoginResult of(
      String accessToken, boolean isNewMember, Long memberId, String nickname) {
    return new LoginResult(accessToken, isNewMember, memberId, nickname);
  }
}
