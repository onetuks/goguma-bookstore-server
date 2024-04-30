package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.restock.model.Restock;

public class RestockFixture {

  public static Restock create(Member member, Book book) {
    return Restock.builder().member(member).book(book).build();
  }
}
