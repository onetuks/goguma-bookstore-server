package com.onetuks.scmapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;

public record AuthorEditResponse(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl) {

  public static AuthorEditResponse from(Author author) {
    return new AuthorEditResponse(
        author.authorId(),
        author.profileImgFilePath().getUrl(),
        author.nickname().nicknameValue(),
        author.introduction(),
        author.instagramUrl());
  }
}
