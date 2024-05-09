package com.onetuks.modulereader.favorite.service.dto.result;

import com.onetuks.modulepersistence.favorite.model.Favorite;

public record FavoritePostResult(long favoriteId) {

  public static FavoritePostResult from(Favorite favorite) {
    return new FavoritePostResult(favorite.getFavoriteId());
  }
}
