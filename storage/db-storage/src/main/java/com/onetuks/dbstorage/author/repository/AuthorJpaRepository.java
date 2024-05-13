package com.onetuks.dbstorage.author.repository;

import com.onetuks.dbstorage.author.entity.AuthorEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, Long> {

  List<AuthorEntity> findByIsEnrollmentPassedTrue();

  Optional<AuthorEntity> findByMemberEntityMemberId(long memberId);
}
