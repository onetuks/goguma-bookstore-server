package com.onetuks.scmapi.author.dto.response;

import com.onetuks.coredomain.author.model.Author;

public record AuthorPostResponse(long authorId) {

  public static AuthorPostResponse from(Author author) {
    return new AuthorPostResponse(author.authorId());
  }
}
