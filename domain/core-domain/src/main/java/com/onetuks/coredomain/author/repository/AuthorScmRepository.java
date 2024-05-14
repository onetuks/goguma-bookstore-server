package com.onetuks.coredomain.author.repository;

import com.onetuks.coredomain.author.model.Author;
import java.util.List;

public interface AuthorScmRepository {

  Author create(Author author);

  Author read(long authorId);

  Author readByMember(long memberId);

  List<Author> readAll();

  Author update(Author author);

  void delete(long authorId);
}
