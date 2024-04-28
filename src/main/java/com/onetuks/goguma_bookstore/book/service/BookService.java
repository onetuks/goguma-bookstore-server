package com.onetuks.goguma_bookstore.book.service;

import com.onetuks.goguma_bookstore.book.service.dto.result.BookGetResult;
import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.repository.BookJpaQueryDslRepository;
import com.onetuks.modulepersistence.book.repository.BookJpaRepository;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.book.vo.SortOrder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  private final BookJpaRepository bookJpaRepository;
  private final BookJpaQueryDslRepository bookJpaQueryDslRepository;

  public BookService(
      BookJpaRepository bookJpaRepository, BookJpaQueryDslRepository bookJpaQueryDslRepository) {
    this.bookJpaRepository = bookJpaRepository;
    this.bookJpaQueryDslRepository = bookJpaQueryDslRepository;
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

  @Transactional(readOnly = true)
  public Page<BookGetResult> readBooks(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      SortOrder sortOrder,
      Pageable pageable) {
    Pageable newPageable =
        PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(),
            sortOrder.getDirection(), sortOrder.getProperty());
    return bookJpaQueryDslRepository
        .findByConditionsAndOrderByCriterias(
            title, authorNickname, category, onlyPromotion, exceptSoldOut, sortOrder, newPageable)
        .map(BookGetResult::from);
  }
}
