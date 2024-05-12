package com.onetuks.coredomain;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.order.entity.ItemEntity;
import com.onetuks.dbstorage.order.entity.OrderEntity;

public class ItemFixture {

  public static ItemEntity create(BookEntity bookEntity, MemberEntity memberEntity, OrderEntity orderEntity) {
    return ItemEntity.builder().book(bookEntity).member(memberEntity).order(orderEntity).build();
  }
}
