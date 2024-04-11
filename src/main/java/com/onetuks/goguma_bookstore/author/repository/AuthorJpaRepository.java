package com.onetuks.goguma_bookstore.author.repository;

import com.onetuks.goguma_bookstore.author.model.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {

  List<Author> findAuthorsByEnrollmentPassedFalse();
}
