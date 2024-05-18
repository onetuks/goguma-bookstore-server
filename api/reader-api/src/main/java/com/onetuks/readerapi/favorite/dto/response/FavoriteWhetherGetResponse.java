package com.onetuks.readerapi.favorite.dto.response;

import com.onetuks.modulereader.favorite.service.dto.result.FavoriteWhetherGetResult;

public record FavoriteWhetherGetResponse(boolean isFavorited) {

  public static FavoriteWhetherGetResponse from(FavoriteWhetherGetResult result) {
    return new FavoriteWhetherGetResponse(result.isFavorited());
  }
}
