package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.restock.model.Restock;

public class RestockFixture {

  public static Restock create(Member member, Book book) {
    return Restock.builder().member(member).book(book).build();
  }
}
