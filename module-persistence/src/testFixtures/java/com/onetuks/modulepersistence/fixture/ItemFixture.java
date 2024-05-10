package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.order.entity.ItemEntity;
import com.onetuks.modulepersistence.order.entity.OrderEntity;

public class ItemFixture {

  public static ItemEntity create(BookEntity bookEntity, MemberEntity memberEntity, OrderEntity orderEntity) {
    return ItemEntity.builder().book(bookEntity).member(memberEntity).order(orderEntity).build();
  }
}
