package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.entity.BookEntity;
import com.onetuks.modulepersistence.favorite.entity.FavoriteEntity;
import com.onetuks.modulepersistence.member.entity.MemberEntity;

public class FavoriteFixture {

  public static FavoriteEntity create(MemberEntity memberEntity, BookEntity bookEntity) {
    return FavoriteEntity.builder().member(memberEntity).book(bookEntity).build();
  }
}
