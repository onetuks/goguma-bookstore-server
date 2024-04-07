package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.book.model.Comment;

public class CommentFixture {

  public static Comment create() {
    return Comment.builder()
        .book(BookFixture.create())
        .member(MemberFixture.create())
        .title("최고의 저녁반찬")
        .content("암튼 저녁먹을때 보면 밥도둑임")
        .build();
  }
}
