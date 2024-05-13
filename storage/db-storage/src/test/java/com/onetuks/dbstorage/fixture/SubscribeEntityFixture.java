package com.onetuks.dbstorage.fixture;

import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.subscribe.entity.SubscribeEntity;

public class SubscribeEntityFixture {

  public static SubscribeEntity create(MemberEntity memberEntity, AuthorEntity authorEntity) {
    return new SubscribeEntity(
        memberEntity,
        authorEntity
    );
  }
}
