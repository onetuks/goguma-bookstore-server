package com.onetuks.coredomain;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.favorite.model.Favorite;
import com.onetuks.coredomain.member.model.Member;

public class FavoriteFixture {

  public static Favorite create(Member member, Book book) {
    return new Favorite(
        null,
        member,
        book
    );
  }
}
