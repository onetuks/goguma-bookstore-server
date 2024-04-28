package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.favorite.model.Favorite;
import com.onetuks.modulepersistence.member.model.Member;

public class FavoriteFixture {

  public static Favorite create(Member member, Book book) {
    return Favorite.builder().member(member).book(book).build();
  }
}
