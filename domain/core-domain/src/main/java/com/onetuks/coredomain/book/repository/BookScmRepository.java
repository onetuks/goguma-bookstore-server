package com.onetuks.coredomain.book.repository;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.registration.model.Registration;
import java.util.List;

public interface BookScmRepository {

  Book create(Registration registration);

  Book read(long bookId);

  List<Book> readAll(long authorId);

  Book update(Book book);

  void delete(long bookId);
}
