package com.onetuks.goguma_bookstore.author.repository;

import com.onetuks.goguma_bookstore.author.model.Author;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

  List<Author> findAuthorsByEnrollmentInfoEnrollmentPassedFalse();

  @Modifying
  @Query(
      "DELETE FROM Author a WHERE a.enrollmentInfo.enrollmentPassed = false AND a.enrollmentInfo.enrollmentAt <= :twoWeeksAgo")
  void deleteAuthorsByNotPassedAndEnrollmentAtForTwoWeeks(LocalDateTime twoWeeksAgo);

  List<Author> findAuthorsByEnrollmentInfoEnrollmentPassedTrue();

  Optional<Long> findAuthorIdByMemberMemberId(long memberId);
}
