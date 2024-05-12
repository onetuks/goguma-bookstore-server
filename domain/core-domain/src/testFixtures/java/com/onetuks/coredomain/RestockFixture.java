package com.onetuks.coredomain;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.restock.entity.RestockEntity;

public class RestockFixture {

  public static RestockEntity create(MemberEntity memberEntity, BookEntity bookEntity) {
    return RestockEntity.builder().member(memberEntity).book(bookEntity).build();
  }
}
