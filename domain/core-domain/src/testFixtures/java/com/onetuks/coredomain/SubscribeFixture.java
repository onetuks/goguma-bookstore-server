package com.onetuks.coredomain;

import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import com.onetuks.dbstorage.subscribe.entity.SubscribeEntity;

public class SubscribeFixture {

  public static SubscribeEntity create(MemberEntity memberEntity, AuthorEntity authorEntity) {
    return SubscribeEntity.builder().member(memberEntity).author(authorEntity).build();
  }
}
