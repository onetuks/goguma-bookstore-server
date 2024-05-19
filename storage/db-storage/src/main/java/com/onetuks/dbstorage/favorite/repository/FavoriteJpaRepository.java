package com.onetuks.dbstorage.favorite.repository;

import com.onetuks.dbstorage.favorite.entity.FavoriteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long> {

  boolean existsByMemberEntityMemberIdAndBookEntityBookId(long memberId, long bookId);

  Page<FavoriteEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);
}
