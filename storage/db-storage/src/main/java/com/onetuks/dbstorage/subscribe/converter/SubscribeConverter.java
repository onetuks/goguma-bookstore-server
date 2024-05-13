package com.onetuks.dbstorage.subscribe.converter;

import com.onetuks.coredomain.subscribe.model.Subscribe;
import com.onetuks.dbstorage.author.converter.AuthorConverter;
import com.onetuks.dbstorage.member.converter.MemberConverter;
import com.onetuks.dbstorage.subscribe.entity.SubscribeEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscribeConverter {

  private final MemberConverter memberConverter;
  private final AuthorConverter authorConverter;

  public SubscribeConverter(
      MemberConverter memberConverter,
      AuthorConverter authorConverter) {
    this.memberConverter = memberConverter;
    this.authorConverter = authorConverter;
  }

  public SubscribeEntity toEntity(Subscribe subscribe) {
    return new SubscribeEntity(
        memberConverter.toEntity(subscribe.member()),
        authorConverter.toEntity(subscribe.author())
    );
  }

  public Subscribe toDomain(SubscribeEntity subscribeEntity) {
    return new Subscribe(
        subscribeEntity.getSubscribeId(),
        memberConverter.toDomain(subscribeEntity.getMemberEntity()),
        authorConverter.toDomain(subscribeEntity.getAuthorEntity())
    );
  }
}
