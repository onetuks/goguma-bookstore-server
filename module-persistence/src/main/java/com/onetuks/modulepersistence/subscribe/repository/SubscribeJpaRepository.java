package com.onetuks.modulepersistence.subscribe.repository;

import com.onetuks.modulepersistence.subscribe.model.Subscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeJpaRepository extends JpaRepository<Subscribe, Long> {

  boolean existsByMemberMemberIdAndAuthorAuthorId(long memberId, long authorId);

  Page<Subscribe> findAllByMemberMemberId(long memberId, Pageable pageable);
}
