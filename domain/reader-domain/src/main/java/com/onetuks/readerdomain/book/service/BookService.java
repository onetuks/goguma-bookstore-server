package com.onetuks.readerdomain.book.service;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.repository.BookRepository;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Transactional(readOnly = true)
  public Book readBook(long bookId) {
    return bookRepository.read(bookId);
  }

  @Transactional(readOnly = true)
  public Page<Book> readBooks(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      PageOrder pageOrder,
      Pageable pageable) {
    return bookRepository.readAll(
        title, authorNickname, category, onlyPromotion, exceptSoldOut, pageOrder, pageable);
  }
}
