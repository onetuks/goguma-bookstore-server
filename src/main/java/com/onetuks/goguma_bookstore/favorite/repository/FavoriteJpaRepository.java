package com.onetuks.goguma_bookstore.favorite.repository;

import com.onetuks.goguma_bookstore.favorite.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

  boolean existsByMemberMemberIdAndBookBookId(long memberId, long bookId);
}
