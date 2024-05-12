package com.onetuks.coredomain;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.entity.CommentEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;

public class CommentFixture {

  public static CommentEntity create(BookEntity bookEntity, MemberEntity memberEntity) {
    return CommentEntity.builder()
        .book(bookEntity)
        .member(memberEntity)
        .title("최고의 저녁 반찬")
        .content("암튼 저녁먹을때 보면 밥도둑임")
        .build();
  }
}
