package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.favorite.model.Favorite;

public class FavoriteFixture {

  public static Favorite create(Member member, Book book) {
    return Favorite.builder().member(member).book(book).build();
  }
}
