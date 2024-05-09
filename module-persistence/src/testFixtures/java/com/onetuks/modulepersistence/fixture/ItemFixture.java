package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.order.model.Item;
import com.onetuks.modulepersistence.order.model.Order;

public class ItemFixture {

  public static Item create(Book book, Member member, Order order) {
    return Item.builder().book(book).member(member).order(order).build();
  }
}
