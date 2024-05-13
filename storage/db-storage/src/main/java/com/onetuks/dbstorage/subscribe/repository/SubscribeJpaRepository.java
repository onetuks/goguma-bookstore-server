package com.onetuks.dbstorage.subscribe.repository;

import com.onetuks.dbstorage.subscribe.entity.SubscribeEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeJpaRepository extends JpaRepository<SubscribeEntity, Long> {

  boolean existsByMemberEntityMemberIdAndAuthorEntityAuthorId(long memberId, long authorId);

  List<SubscribeEntity> findAllByMemberEntityMemberId(long memberId);
}
