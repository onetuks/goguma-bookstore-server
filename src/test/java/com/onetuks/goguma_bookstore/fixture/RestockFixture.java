package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.restock.model.Restock;

public class RestockFixture {

  public static Restock create() {
    return Restock.builder().member(MemberFixture.create()).book(BookFixture.create()).build();
  }
}
