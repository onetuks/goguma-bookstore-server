package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.modulepersistence.author.model.Author;
import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.subscribe.model.Subscribe;

public class SubscribeFixture {

  public static Subscribe create(Member member, Author author) {
    return Subscribe.builder().member(member).author(author).build();
  }
}
