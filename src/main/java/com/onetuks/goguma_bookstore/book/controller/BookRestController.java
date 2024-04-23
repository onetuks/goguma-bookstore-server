package com.onetuks.goguma_bookstore.book.controller;

import com.onetuks.goguma_bookstore.book.controller.dto.response.BookResponse;
import com.onetuks.goguma_bookstore.book.service.BookService;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/books")
public class BookRestController {

  private final BookService bookService;

  public BookRestController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping(path = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookResponse> getBook(@PathVariable(name = "bookId") Long bookId) {
    BookResult result = bookService.readBook(bookId);
    BookResponse response = BookResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
