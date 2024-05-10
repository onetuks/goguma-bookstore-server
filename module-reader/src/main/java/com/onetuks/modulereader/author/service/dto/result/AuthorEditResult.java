package com.onetuks.modulereader.author.service.dto.result;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;

public record AuthorEditResult(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl) {

  public static AuthorEditResult from(AuthorEntity authorEntity) {
    return new AuthorEditResult(
        authorEntity.getAuthorId(),
        authorEntity.getProfileImgUrl(),
        authorEntity.getNickname(),
        authorEntity.getIntroduction(),
        authorEntity.getInstagramUrl());
  }
}
