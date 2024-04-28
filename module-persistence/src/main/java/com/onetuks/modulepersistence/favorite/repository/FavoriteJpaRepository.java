package com.onetuks.modulepersistence.favorite.repository;

import com.onetuks.modulepersistence.favorite.model.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

  boolean existsByMemberMemberIdAndBookBookId(long memberId, long bookId);

  Page<Favorite> findAllByMemberMemberId(long memberId, Pageable pageable);
}
