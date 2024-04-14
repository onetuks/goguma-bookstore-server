package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.subscribe.model.Subscribe;

public class SubscribeFixture {

  public static Subscribe create(Member member, Author author) {
    return Subscribe.builder().member(member).author(author).build();
  }
}
