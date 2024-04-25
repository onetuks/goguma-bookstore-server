package com.onetuks.goguma_bookstore.favorite.controller.dto.response;

import com.onetuks.goguma_bookstore.favorite.service.dto.result.FavoritePostResult;

public record FavoritePostResponse(long favoriteId) {

  public static FavoritePostResponse from(FavoritePostResult result) {
    return new FavoritePostResponse(result.favoriteId());
  }
}
