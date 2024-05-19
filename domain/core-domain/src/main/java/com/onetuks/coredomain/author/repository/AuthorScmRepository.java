package com.onetuks.coredomain.author.repository;

import com.onetuks.coredomain.author.model.Author;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorScmRepository {

  Author create(Author author);

  Author read(long authorId);

  Author readByMember(long memberId);

  List<Author> readAll();

  Page<Author> readAll(Pageable pageable);

  Author update(Author author);

  void delete(long authorId);
}
