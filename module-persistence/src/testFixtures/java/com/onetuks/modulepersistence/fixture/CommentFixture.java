package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.model.Comment;
import com.onetuks.modulepersistence.member.model.Member;

public class CommentFixture {

  public static Comment create(Book book, Member member) {
    return Comment.builder()
        .book(book)
        .member(member)
        .title("최고의 저녁 반찬")
        .content("암튼 저녁먹을때 보면 밥도둑임")
        .build();
  }
}
