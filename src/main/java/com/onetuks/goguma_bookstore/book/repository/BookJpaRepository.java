package com.onetuks.goguma_bookstore.book.repository;

import com.onetuks.goguma_bookstore.book.model.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

  Optional<Book> findByBookConceptualInfoIsbn(String isbn);
}
