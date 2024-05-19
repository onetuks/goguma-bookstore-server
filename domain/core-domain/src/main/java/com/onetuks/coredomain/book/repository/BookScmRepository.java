package com.onetuks.coredomain.book.repository;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.registration.model.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookScmRepository {

  Book create(Registration registration);

  Book read(long bookId);

  Page<Book> readAll(long authorId, Pageable pageable);

  Book update(Book book);

  void delete(long bookId);
}
