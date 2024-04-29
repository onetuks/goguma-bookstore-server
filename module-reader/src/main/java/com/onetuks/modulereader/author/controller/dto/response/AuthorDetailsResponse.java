package com.onetuks.modulereader.author.controller.dto.response;

import com.onetuks.modulereader.author.service.dto.result.AuthorDetailsResult;
import org.springframework.data.domain.Page;

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

  public record AuthorDetailsResponses(Page<AuthorDetailsResponse> responses) {

    public static AuthorDetailsResponses from(Page<AuthorDetailsResult> results) {
      return new AuthorDetailsResponses(results.map(AuthorDetailsResponse::from));
    }
  }
}
