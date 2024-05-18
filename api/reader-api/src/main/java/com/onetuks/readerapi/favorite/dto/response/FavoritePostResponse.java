package com.onetuks.readerapi.favorite.dto.response;

import com.onetuks.coredomain.favorite.model.Favorite;

public record FavoritePostResponse(long favoriteId) {

  public static FavoritePostResponse from(Favorite favorite) {
    return new FavoritePostResponse(favorite.favoriteId());
  }
}
