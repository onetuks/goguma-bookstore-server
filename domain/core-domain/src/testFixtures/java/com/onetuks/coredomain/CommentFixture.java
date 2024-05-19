package com.onetuks.coredomain;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.model.Comment;
import com.onetuks.coredomain.member.model.Member;

public class CommentFixture {

  public static Comment create(Book book, Member member) {
    return new Comment(null, book, member, "최고의 저녁반찬", "이거면 저녁식사 뚝딱!");
  }
}
