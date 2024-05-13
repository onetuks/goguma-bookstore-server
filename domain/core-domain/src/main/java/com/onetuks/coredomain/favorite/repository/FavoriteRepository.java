package com.onetuks.coredomain.favorite.repository;

import com.onetuks.coredomain.favorite.model.Favorite;
import java.util.List;

public interface FavoriteRepository {

  Favorite create(Favorite favorite);

  Favorite read(long favoriteId);

  boolean readExistence(long memberId, long bookId);

  List<Favorite> readAll(long memberId);

  void delete(long favoriteId);
}
