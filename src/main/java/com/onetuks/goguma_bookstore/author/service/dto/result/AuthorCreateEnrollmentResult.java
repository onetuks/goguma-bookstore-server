package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;

public record AuthorCreateEnrollmentResult(
    long authorId, String profileImgUrl, String nickname, String introduction) {

  public static AuthorCreateEnrollmentResult from(Author author) {
    return new AuthorCreateEnrollmentResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction());
  }
}
