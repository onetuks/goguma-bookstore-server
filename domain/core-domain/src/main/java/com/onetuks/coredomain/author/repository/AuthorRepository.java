package com.onetuks.coredomain.author.repository;

import com.onetuks.coredomain.author.model.Author;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository {

  Author create(Author author);

  Author read(long authorId);

  List<Author> readAllEnrollmentPassed();

  Author update(Author author);
}
