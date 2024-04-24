package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookGetResult;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  private final BookJpaRepository bookJpaRepository;

  public BookService(BookJpaRepository bookJpaRepository) {
    this.bookJpaRepository = bookJpaRepository;
  }

  @Transactional(readOnly = true)
  public BookGetResult readBook(long bookId) {
    Book book =
        bookJpaRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("해당 도서가 존재하지 않습니다."));

    book.getBookStatics().increaseViewCount();

    return BookGetResult.from(book);
  }
}
