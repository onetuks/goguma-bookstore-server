package com.onetuks.goguma_bookstore.author_debut.service.dto.result;

import com.onetuks.goguma_bookstore.author_debut.model.Author;

public record AuthorDebutCreateResult(
    long memberId, String profileImgUri, String nickname, String introduction) {

  public static AuthorDebutCreateResult from(Author author) {
    return new AuthorDebutCreateResult(
        author.getMember().getMemberId(),
        author.getProfileImgUri(),
        author.getNickname(),
        author.getIntroduction());
  }
}
