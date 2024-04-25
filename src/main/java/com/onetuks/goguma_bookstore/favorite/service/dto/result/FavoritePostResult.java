package com.onetuks.goguma_bookstore.favorite.service.dto.result;

import com.onetuks.goguma_bookstore.favorite.model.Favorite;

public record FavoritePostResult(long favoriteId) {

  public static FavoritePostResult from(Favorite favorite) {
    return new FavoritePostResult(favorite.getFavoriteId());
  }
}
