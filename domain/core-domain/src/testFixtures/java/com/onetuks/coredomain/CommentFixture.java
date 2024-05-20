package com.onetuks.coredomain;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.comment.model.Comment;
import com.onetuks.coredomain.member.model.Member;

public class CommentFixture {

  public static Comment create(Long commentId, Book book, Member member) {
    return new Comment(commentId, book, member, "최고의 저녁반찬", "이거면 저녁식사 뚝딱!");
  }
}
