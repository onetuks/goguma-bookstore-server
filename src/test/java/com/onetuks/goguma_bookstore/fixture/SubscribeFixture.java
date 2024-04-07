package com.onetuks.goguma_bookstore.fixture;

import com.onetuks.goguma_bookstore.subscribe.model.Subscribe;

public class SubscribeFixture {

  public static Subscribe create() {
    return Subscribe.builder()
        .member(MemberFixture.create())
        .author(AuthorFixture.create())
        .build();
  }
}
