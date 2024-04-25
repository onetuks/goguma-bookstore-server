package com.onetuks.goguma_bookstore.favorite.service.dto.result;

public record FavoriteWhetherGetResult(boolean isFavorited) {

  public static FavoriteWhetherGetResult from(boolean isFavorited) {
    return new FavoriteWhetherGetResult(isFavorited);
  }
}
