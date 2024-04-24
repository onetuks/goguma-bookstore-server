package com.onetuks.goguma_bookstore.book.controller;

import com.onetuks.goguma_bookstore.book.controller.dto.response.BookGetResponse;
import com.onetuks.goguma_bookstore.book.controller.dto.response.BookGetResponse.BookGetResponses;
import com.onetuks.goguma_bookstore.book.service.BookService;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookGetResult;
import com.onetuks.goguma_bookstore.book.vo.Category;
import com.onetuks.goguma_bookstore.book.vo.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  /**
   * 도서 목록 조회
   *
   * @param title : 도서 제목
   * @param authorNickname : 작가 닉네임
   * @param onlyPromotion : 프로모션 도서만 조회 여부
   * @param category : 카테고리
   * @param exceptSoldOut : 품절 도서 제외 여부
   * @param pageable : 페이지 정보
   * @return BookGetResponses
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookGetResponses> getBooks(
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "authorNickname", required = false) String authorNickname,
      @RequestParam(name = "category", required = false) Category category,
      @RequestParam(name = "only-promotion", required = false, defaultValue = "false")
          Boolean onlyPromotion,
      @RequestParam(name = "except-sold-out", required = false, defaultValue = "false")
          Boolean exceptSoldOut,
      @RequestParam(name = "sort-order", required = false, defaultValue = "DATE")
          SortOrder sortOrder,
      @PageableDefault(sort = "bookId", direction = Direction.DESC) Pageable pageable) {
    Page<BookGetResult> results =
        bookService.readBooks(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, sortOrder, pageable);
    BookGetResponses responses = BookGetResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
