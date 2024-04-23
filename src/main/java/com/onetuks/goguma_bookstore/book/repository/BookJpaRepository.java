package com.onetuks.goguma_bookstore.book.repository;

import com.onetuks.goguma_bookstore.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

  Page<Book> findAllByAuthorAuthorId(long authorId, Pageable pageable);
}
