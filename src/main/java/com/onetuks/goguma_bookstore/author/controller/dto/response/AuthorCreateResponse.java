package com.onetuks.goguma_bookstore.author.controller.dto.response;

import com.onetuks.goguma_bookstore.author.service.dto.result.AuthorCreateResult;

public record AuthorCreateResponse(long authorId) {

  public static AuthorCreateResponse from(AuthorCreateResult result) {
    return new AuthorCreateResponse(result.authorId());
  }
}
