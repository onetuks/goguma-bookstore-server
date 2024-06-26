package com.onetuks.coredomain.book.model;

public record BookStatics(
    Long bookStaticsId,
    long favoriteCount,
    long viewCount,
    long salesCount,
    long commentCount,
    long restockCount,
    float reviewScore) {

  public static BookStatics init() {
    return new BookStatics(null, 0, 0, 0, 0, 0, 0);
  }
}
