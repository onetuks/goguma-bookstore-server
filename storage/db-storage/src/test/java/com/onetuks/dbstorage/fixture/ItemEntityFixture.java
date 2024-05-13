package com.onetuks.dbstorage.fixture;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.order.entity.ItemEntity;
import com.onetuks.dbstorage.order.entity.OrderEntity;

public class ItemEntityFixture {

  public static ItemEntity create(OrderEntity orderEntity, MemberEntity memberEntity, BookEntity bookEntity) {
    return new ItemEntity(
        orderEntity,
        memberEntity,
        bookEntity
    );
  }
}
