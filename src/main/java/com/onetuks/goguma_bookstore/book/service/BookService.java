package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookResult;
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
  public BookResult readBook(long bookId) {
    return bookJpaRepository
        .findById(bookId)
        .map(BookResult::from)
        .orElseThrow(() -> new EntityNotFoundException("해당 도서가 존재하지 않습니다."));
  }
}
