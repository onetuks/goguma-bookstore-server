package com.onetuks.modulereader.subscribe.service.dto.result;

import com.onetuks.modulepersistence.subscribe.model.Subscribe;

public record SubscribePostResult(long subscribeId, long memberId, long authorId) {

  public static SubscribePostResult from(Subscribe subscribe) {
    return new SubscribePostResult(
        subscribe.getSubscribeId(),
        subscribe.getMember().getMemberId(),
        subscribe.getAuthor().getAuthorId());
  }
}
