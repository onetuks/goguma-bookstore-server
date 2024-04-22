package com.onetuks.goguma_bookstore.book.controller;

import com.onetuks.goguma_bookstore.book.service.BookScmService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/scm/books")
public class BookScmRestController {

  private final BookScmService bookScmService;

  public BookScmRestController(BookScmService bookScmService) {
    this.bookScmService = bookScmService;
  }
}
