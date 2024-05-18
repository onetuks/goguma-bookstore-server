package com.onetuks.scmapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;

public record AuthorCreateResponse(long authorId) {

  public static AuthorCreateResponse from(Author author) {
    return new AuthorCreateResponse(author.authorId());
  }
}
