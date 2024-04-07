package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.order.model.Item;

public class ItemFixture {

  public static Item create() {
    return Item.builder()
        .book(BookFixture.create())
        .member(MemberFixture.create())
        .order(OrderFixture.create())
        .build();
  }
}
