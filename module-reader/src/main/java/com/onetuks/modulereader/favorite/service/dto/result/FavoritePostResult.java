package com.onetuks.modulereader.favorite.service.dto.result;

import com.onetuks.modulepersistence.favorite.entity.FavoriteEntity;

public record FavoritePostResult(long favoriteId) {

  public static FavoritePostResult from(FavoriteEntity favoriteEntity) {
    return new FavoritePostResult(favoriteEntity.getFavoriteId());
  }
}
