package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.order.model.Item;
import com.onetuks.goguma_bookstore.order.model.Order;

public class ItemFixture {

  public static Item create(Book book, Member member, Order order) {
    return Item.builder().book(book).member(member).order(order).build();
  }
}
