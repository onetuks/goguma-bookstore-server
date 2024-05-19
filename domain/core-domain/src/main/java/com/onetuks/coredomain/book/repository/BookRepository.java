package com.onetuks.coredomain.book.repository;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {

  Book read(long bookId);

  Page<Book> readAll(
      String title,
      String authorNickname,
      Category category,
      boolean onlyPromotion,
      boolean exceptSoldOut,
      PageOrder pageOrder,
      Pageable pageable);
}
