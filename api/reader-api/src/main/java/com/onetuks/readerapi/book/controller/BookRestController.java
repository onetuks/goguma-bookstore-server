package com.onetuks.readerapi.book.controller;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import com.onetuks.readerapi.book.dto.response.BookGetResponse;
import com.onetuks.readerapi.book.dto.response.BookGetResponse.BookGetResponses;
import com.onetuks.readerdomain.book.service.BookService;
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
   * @param bookId : 책 ID
   * @return 200 OK
   */
  @GetMapping(path = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BookGetResponse> getBook(
      @PathVariable(name = "bookId") Long bookId
  ) {
    Book result = bookService.readBook(bookId);
    BookGetResponse response = BookGetResponse.from(result);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 도서 목록 조회
   * @param title : 책 제목
   * @param authorNickname : 작가 닉네임
   * @param category : 카테고리
   * @param onlyPromotion : 프로모션 상품만 조회
   * @param exceptSoldOut : 품절 상품 제외
   * @param pageOrder : 페이지 정렬 기준
   * @param pageable : 페이지 정보
   * @return 200 OK
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
      @RequestParam(name = "pageOrder", required = false, defaultValue = "DATE")
      PageOrder pageOrder,
      @PageableDefault(sort = "bookId", direction = Direction.DESC) Pageable pageable) {
    Page<Book> results =
        bookService.readBooks(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, pageOrder, pageable);
    BookGetResponses responses = BookGetResponses.from(results);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }
}
