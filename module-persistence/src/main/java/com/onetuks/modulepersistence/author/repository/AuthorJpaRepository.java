package com.onetuks.modulepersistence.author.repository;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, Long> {

  Page<AuthorEntity> findAuthorsByEnrollmentInfoEnrollmentPassedFalse(Pageable pageable);

  Page<AuthorEntity> findAuthorsByEnrollmentInfoEnrollmentPassedTrue(Pageable pageable);

  Optional<AuthorEntity> findByMemberMemberId(long memberId);
}
