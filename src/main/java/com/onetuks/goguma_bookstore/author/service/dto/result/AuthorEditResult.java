package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;

public record AuthorEditResult(
    long authorId, String profileImgUrl, String nickname, String introduction) {

  public static AuthorEditResult from(Author author) {
    return new AuthorEditResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction());
  }
}