package com.onetuks.dbstorage.fixture;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.restock.entity.RestockEntity;

public class RestockEntityFixture {

  public static RestockEntity create(
      Long restockId, MemberEntity memberEntity, BookEntity bookEntity) {
    return new RestockEntity(
        restockId, memberEntity, bookEntity, false, memberEntity.getIsAlarmPermitted());
  }
}
