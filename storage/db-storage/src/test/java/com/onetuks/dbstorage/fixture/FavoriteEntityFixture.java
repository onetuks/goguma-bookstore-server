package com.onetuks.dbstorage.fixture;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.favorite.entity.FavoriteEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;

public class FavoriteEntityFixture {

  public static FavoriteEntity create(MemberEntity memberEntity, BookEntity bookEntity) {
    return new FavoriteEntity(
        memberEntity,
        bookEntity
    );
  }
}
