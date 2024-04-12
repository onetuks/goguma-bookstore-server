package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;

public record AuthorDetailsResult(
    long authorId, String profileImgUrl, String nickname, String introduction) {

  public static AuthorDetailsResult from(Author author) {
    return new AuthorDetailsResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction());
  }
}
