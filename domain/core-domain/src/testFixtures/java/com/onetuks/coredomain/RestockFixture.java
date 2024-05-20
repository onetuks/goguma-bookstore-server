package com.onetuks.coredomain;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.restock.model.Restock;

public class RestockFixture {

  public static Restock create(Long restockId, Member member, Book book) {
    return new Restock(
        restockId,
        member,
        book,
        false,
        member.isAlarmPermitted()
    );
  }
}
