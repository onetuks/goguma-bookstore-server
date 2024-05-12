package com.onetuks.coredomain;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.favorite.entity.FavoriteEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;

public class FavoriteFixture {

  public static FavoriteEntity create(MemberEntity memberEntity, BookEntity bookEntity) {
    return FavoriteEntity.builder().member(memberEntity).book(bookEntity).build();
  }
}
