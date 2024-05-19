package com.onetuks.scmapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;

public record AuthorPatchResponse(
    long authorId,
    String profileImgUrl,
    String nickname,
    String introduction,
    String instagramUrl) {

  public static AuthorPatchResponse from(Author author) {
    return new AuthorPatchResponse(
        author.authorId(),
        author.profileImgFilePath().getUrl(),
        author.nickname().nicknameValue(),
        author.introduction(),
        author.instagramUrl());
  }
}
