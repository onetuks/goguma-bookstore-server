package com.onetuks.dbstorage.subscribe.repository;

import com.onetuks.dbstorage.subscribe.entity.SubscribeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeJpaRepository extends JpaRepository<SubscribeEntity, Long> {

  boolean existsByMemberEntityMemberIdAndAuthorEntityAuthorId(long memberId, long authorId);

  Page<SubscribeEntity> findAllByMemberEntityMemberId(long memberId, Pageable pageable);
}
