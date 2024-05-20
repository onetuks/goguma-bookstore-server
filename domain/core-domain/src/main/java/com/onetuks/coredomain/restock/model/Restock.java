package com.onetuks.coredomain.restock.model;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.member.model.Member;

public record Restock(
    Long restockId,
    Member member,
    Book book,
    boolean isFulfilled,
    boolean isAlarmPermitted
) {

  public Restock changeAlarmPermitted(boolean isAlarmPermitted) {
    return new Restock(
        restockId(),
        member(),
        book(),
        isFulfilled(),
        isAlarmPermitted
    );
  }
}
