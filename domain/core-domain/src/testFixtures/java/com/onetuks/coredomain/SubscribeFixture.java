package com.onetuks.coredomain;

import com.onetuks.coredomain.author.model.Author;
import com.onetuks.coredomain.member.model.Member;
import com.onetuks.coredomain.subscribe.model.Subscribe;

public class SubscribeFixture {

  public static Subscribe create(long subscribeId, Member member, Author author) {
    return new Subscribe(subscribeId, member, author);
  }
}
