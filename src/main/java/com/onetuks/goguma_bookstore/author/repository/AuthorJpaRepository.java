package com.onetuks.goguma_bookstore.author.repository;

import com.onetuks.goguma_bookstore.author.model.Author;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

  Page<Author> findAuthorsByEnrollmentInfoEnrollmentPassedFalse(Pageable pageable);

  Page<Author> findAuthorsByEnrollmentInfoEnrollmentPassedTrue(Pageable pageable);

  Optional<Author> findByMemberMemberId(long memberId);
}
