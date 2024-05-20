package com.onetuks.scmapi.restock.controller.dto.response;

import com.onetuks.coredomain.book.model.Book;
import org.springframework.data.domain.Page;

public record RestockScmResponse(long bookId, String title, long restockCount) {

  public static RestockScmResponse from(Book book) {
    return new RestockScmResponse(
        book.bookId(), book.bookConceptualInfo().title(), book.bookStatics().restockCount());
  }

  public record RestockScmResponses(Page<RestockScmResponse> responses) {

    public static RestockScmResponses from(Page<Book> results) {
      return new RestockScmResponses(results.map(RestockScmResponse::from));
    }
  }
}
