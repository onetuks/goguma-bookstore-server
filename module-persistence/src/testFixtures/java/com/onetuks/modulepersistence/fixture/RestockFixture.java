package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.restock.entity.RestockEntity;

public class RestockFixture {

  public static RestockEntity create(MemberEntity memberEntity, BookEntity bookEntity) {
    return RestockEntity.builder().member(memberEntity).book(bookEntity).build();
  }
}
