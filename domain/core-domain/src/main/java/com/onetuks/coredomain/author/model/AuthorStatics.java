package com.onetuks.coredomain.author.model;

public record AuthorStatics(
    Long authorStaticsId, long subscribeCount, long bookCount) {

  public static AuthorStatics init() {
    return new AuthorStatics(null, 0, 0);
  }
}
