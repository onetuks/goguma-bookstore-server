package com.onetuks.goguma_bookstore.favorite.controller.dto.response;

import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoriteWhetherGetResult;

public record FavoriteWhetherGetResponse(boolean isFavorited) {

  public static FavoriteWhetherGetResponse from(FavoriteWhetherGetResult result) {
    return new FavoriteWhetherGetResponse(result.isFavorited());
  }
}
