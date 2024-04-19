package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorDetailsResult;
import java.util.List;

public record AuthorDetailsResponse(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    long subscribeCount,
    long bookCount,
    long restockCount) {

  public static AuthorDetailsResponse from(AuthorDetailsResult result) {
    return new AuthorDetailsResponse(
        result.authorId(),
        result.profileImgUrl(),
        result.nickname(),
        result.introduction(),
        result.instagramUrl(),
        result.subscribeCount(),
        result.bookCount(),
        result.restockCount());
  }

  public record AuthorDetailsResponses(List<AuthorDetailsResponse> responses) {

    public static AuthorDetailsResponses from(List<AuthorDetailsResult> results) {
      return new AuthorDetailsResponses(results.stream().map(AuthorDetailsResponse::from).toList());
    }
  }
}
