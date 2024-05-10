package com.onetuks.readerapi.controller.dto.response;

import com.onetuks.modulereader.favorite.service.dto.result.FavoritePostResult;

public record FavoritePostResponse(long favoriteId) {

  public static FavoritePostResponse from(FavoritePostResult result) {
    return new FavoritePostResponse(result.favoriteId());
  }
}
