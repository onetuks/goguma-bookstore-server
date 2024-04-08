package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;

public record AuthorCreateResult(
    long authorId, String profileImgUrl, String nickname, String introduction) {

  public static AuthorCreateResult from(Author author) {
    return new AuthorCreateResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction());
  }
}
