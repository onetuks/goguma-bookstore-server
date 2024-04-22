package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookJpaRepository bookJpaRepository;

  public BookService(BookJpaRepository bookJpaRepository) {
    this.bookJpaRepository = bookJpaRepository;
  }
}
