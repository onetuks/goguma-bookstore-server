package com.onetuks.dbstorage.favorite.repository;

import com.onetuks.dbstorage.favorite.entity.FavoriteEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long> {

  boolean existsByMemberEntityMemberIdAndBookEntityBookId(long memberId, long bookId);

  List<FavoriteEntity> findAllByMemberEntityMemberId(long memberId);
}
