package com.onetuks.modulereader.subscribe.service.dto.result;

import com.onetuks.modulepersistence.subscribe.model.Subscribe;

public record SubscribeResult(long subscribeId, long memberId, long authorId) {

  public static SubscribeResult from(Subscribe subscribe) {
    return new SubscribeResult(
        subscribe.getSubscribeId(),
        subscribe.getMember().getMemberId(),
        subscribe.getAuthor().getAuthorId());
  }
}
