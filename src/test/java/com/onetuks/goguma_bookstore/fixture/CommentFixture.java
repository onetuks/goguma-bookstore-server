package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.Comment;
import com.onetuks.goguma_bookstore.member.model.Member;

public class CommentFixture {

  public static Comment create(Book book, Member member) {
    return Comment.builder()
        .book(book)
        .member(member)
        .title("최고의 저녁반찬")
        .content("암튼 저녁먹을때 보면 밥도둑임")
        .build();
  }
}
