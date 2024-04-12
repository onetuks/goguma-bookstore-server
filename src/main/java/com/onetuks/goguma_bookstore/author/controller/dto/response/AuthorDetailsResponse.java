package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;

public record AuthorDetailsResponse(
    long authorId, String profileImgUrl, String nickname, String introduction) {

  public static AuthorDetailsResponse from(AuthorDetailsResult result) {
    return new AuthorDetailsResponse(
        result.authorId(), result.profileImgUrl(), result.nickname(), result.introduction());
  }
}
