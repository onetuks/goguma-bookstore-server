package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.favorite.model.Favorite;

public class FavoriteFixture {

  public static Favorite create() {
    return Favorite.builder().member(MemberFixture.create()).book(BookFixture.create()).build();
  }
}
