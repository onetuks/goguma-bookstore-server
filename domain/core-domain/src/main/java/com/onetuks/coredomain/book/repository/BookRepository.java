package com.onetuks.coredomain.book.repository;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.coreobj.enums.book.PageOrder;
import java.util.List;

public interface BookRepository {

  Book read(long bookId);

  List<Book> read(
      String title, String authorNickname, Category category,
      boolean onlyPromotion, boolean exceptSoldOut, PageOrder pageOrder);
}
