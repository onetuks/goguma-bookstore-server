package com.onetuks.coredomain.author.repository;

import com.onetuks.coredomain.author.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository {

  Author create(Author author);

  Author read(long authorId);

  Page<Author> readAllEnrollmentPassed(Pageable pageable);

  Author update(Author author);
}
