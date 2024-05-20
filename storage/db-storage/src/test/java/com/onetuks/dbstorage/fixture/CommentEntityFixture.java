package com.onetuks.dbstorage.fixture;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.comment.entity.CommentEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;

public class CommentEntityFixture {

  public static CommentEntity create(
      Long commentId, BookEntity bookEntity, MemberEntity memberEntity) {
    return new CommentEntity(commentId, bookEntity, memberEntity, "최고의 저녁 반찬", "암튼 저녁먹을때 보면 밥도둑임");
  }
}
