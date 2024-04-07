package com.onetuks.goguma_bookstore.author_debut.controller.dto.response;

import com.onetuks.goguma_bookstore.author_debut.service.dto.result.AuthorDebutCreateResult;

public record AuthorDebutCreateResponse(Long memberId) {

  public static AuthorDebutCreateResponse from(AuthorDebutCreateResult result) {
    return new AuthorDebutCreateResponse(result.memberId());
  }
}
