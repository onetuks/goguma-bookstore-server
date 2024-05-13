package com.onetuks.coredomain.author.repository;

import com.onetuks.coredomain.author.model.Author;
import java.util.List;

public interface AuthorScmRepository {

  Author create(Author author);

  Author read(long authorId);

  List<Author> readAll();

  void delete(long authorId);
}
