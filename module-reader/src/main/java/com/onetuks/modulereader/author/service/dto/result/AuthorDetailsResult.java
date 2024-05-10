package com.onetuks.modulereader.author.service.dto.result;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;

public record AuthorDetailsResult(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl,
    long subscribeCount,
    long bookCount,
    long restockCount) {

  public static AuthorDetailsResult from(AuthorEntity authorEntity) {
    return new AuthorDetailsResult(
        authorEntity.getAuthorId(),
        authorEntity.getProfileImgUrl(),
        authorEntity.getNickname(),
        authorEntity.getIntroduction(),
        authorEntity.getInstagramUrl(),
        authorEntity.getSubscribeCount(),
        authorEntity.getBookCount(),
        authorEntity.getRestockCount());
  }
}
