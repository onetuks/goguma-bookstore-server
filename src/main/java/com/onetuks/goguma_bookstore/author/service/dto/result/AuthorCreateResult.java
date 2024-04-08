package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;

public record AuthorCreateResult(
    long authorId, String profileImgUri, String nickname, String introduction) {

  public static AuthorCreateResult from(Author author) {
    return new AuthorCreateResult(
        author.getAuthorId(),
        author.getProfileImgUri(),
        author.getNickname(),
        author.getIntroduction());
  }
}
