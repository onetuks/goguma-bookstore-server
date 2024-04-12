package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.author.model.AuthorStatics;

public record AuthorDetailsResult(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    long subscribeCount,
    long bookCount,
    long restockCount) {

  public static AuthorDetailsResult from(Author author) {
    AuthorStatics authorStatics = author.getAuthorStatics();

    return new AuthorDetailsResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction(),
        authorStatics.getSubscribeCount(),
        authorStatics.getBookCount(),
        authorStatics.getRestockCount());
  }
}
