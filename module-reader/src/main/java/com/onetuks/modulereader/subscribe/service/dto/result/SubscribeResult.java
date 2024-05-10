package com.onetuks.modulereader.subscribe.service.dto.result;

import com.onetuks.modulepersistence.subscribe.entity.SubscribeEntity;

public record SubscribeResult(long subscribeId, long memberId, long authorId) {

  public static SubscribeResult from(SubscribeEntity subscribeEntity) {
    return new SubscribeResult(
        subscribeEntity.getSubscribeId(),
        subscribeEntity.getMemberEntity().getMemberId(),
        subscribeEntity.getAuthorEntity().getAuthorId());
  }
}
