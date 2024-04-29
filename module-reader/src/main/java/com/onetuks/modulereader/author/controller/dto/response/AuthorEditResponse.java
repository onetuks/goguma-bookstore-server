package com.onetuks.modulereader.author.controller.dto.response;

import com.onetuks.modulereader.author.service.dto.result.AuthorEditResult;

public record AuthorEditResponse(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl) {

  public static AuthorEditResponse from(AuthorEditResult result) {
    return new AuthorEditResponse(
        result.authorId(),
        result.profileImgUrl(),
        result.nickname(),
        result.introduction(),
        result.instagramUrl());
  }
}
