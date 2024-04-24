package com.onetuks.goguma_bookstore.book.controller;

import com.onetuks.goguma_bookstore.book.controller.dto.response.BookGetResponse;
import com.onetuks.goguma_bookstore.book.service.BookService;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookGetResult;
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

  /**
   * 도서 상세 조회
   *
   * @param bookId : 도서 아이디
   * @return BookGetResponse
   */
  @GetMapping(path = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookGetResponse> getBook(@PathVariable(name = "bookId") Long bookId) {
    BookGetResult result = bookService.readBook(bookId);
    BookGetResponse response = BookGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
