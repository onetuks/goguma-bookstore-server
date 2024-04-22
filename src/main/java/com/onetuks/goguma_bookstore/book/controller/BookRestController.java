package com.onetuks.goguma_bookstore.book.controller;

import com.onetuks.goguma_bookstore.book.service.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/books")
public class BookRestController {

  private final BookService bookService;

  public BookRestController(BookService bookService) {
    this.bookService = bookService;
  }
}
