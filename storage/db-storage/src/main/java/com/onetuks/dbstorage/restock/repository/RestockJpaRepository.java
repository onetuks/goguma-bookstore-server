package com.onetuks.dbstorage.restock.repository;

import com.onetuks.dbstorage.restock.entity.RestockEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestockJpaRepository extends JpaRepository<RestockEntity, Long> {

  Page<RestockEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);

  long countByBookEntityAuthorEntityMemberEntityMemberId(long memberId);

  Page<RestockEntity> findAllByBookEntityAuthorEntityMemberEntityMemberId(
      long memberId, Pageable pageable);
}
