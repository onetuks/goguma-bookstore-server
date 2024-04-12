package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorEditResult;

public record AuthorEditResponse(
    long authorId, String profileImgUrl, String nickname, String introduction) {

  public static AuthorEditResponse from(AuthorEditResult result) {
    return new AuthorEditResponse(
        result.authorId(), result.profileImgUrl(), result.nickname(), result.introduction());
  }
}
