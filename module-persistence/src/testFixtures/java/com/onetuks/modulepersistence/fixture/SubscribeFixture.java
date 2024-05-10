package com.onetuks.modulepersistence.fixture;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.subscribe.entity.SubscribeEntity;

public class SubscribeFixture {

  public static SubscribeEntity create(MemberEntity memberEntity, AuthorEntity authorEntity) {
    return SubscribeEntity.builder().member(memberEntity).author(authorEntity).build();
  }
}
