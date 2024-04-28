package com.onetuks.goguma_bookstore.author.service.dto.result;

import com.onetuks.modulepersistence.author.model.Author;

public record AuthorDetailsResult(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    long subscribeCount,
    long bookCount,
    long restockCount) {

  public static AuthorDetailsResult from(Author author) {
    return new AuthorDetailsResult(
        author.getAuthorId(),
        author.getProfileImgUrl(),
        author.getNickname(),
        author.getIntroduction(),
        author.getInstagramUrl(),
        author.getSubscribeCount(),
        author.getBookCount(),
        author.getRestockCount());
  }
}
