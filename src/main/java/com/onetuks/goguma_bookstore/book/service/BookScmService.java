package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.repository.BookJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class BookScmService {

  private final BookJpaRepository bookJpaRepository;

  public BookScmService(BookJpaRepository bookJpaRepository) {
    this.bookJpaRepository = bookJpaRepository;
  }
}
