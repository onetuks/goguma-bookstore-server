package com.onetuks.coredomain.favorite.repository;

import com.onetuks.coredomain.favorite.model.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository {

  Favorite create(Favorite favorite);

  Favorite read(long favoriteId);

  boolean readExistence(long memberId, long bookId);

  Page<Favorite> readAll(long memberId, Pageable pageable);

  void delete(long favoriteId);
}
