package com.onetuks.modulereader.subscribe.controller.dto.response;

import com.onetuks.modulereader.subscribe.service.dto.result.SubscribePostResult;

public record SubscribeResponse(long subscribeId, long memberId, long authorId) {

  public static SubscribeResponse from(SubscribePostResult result) {
    return new SubscribeResponse(result.subscribeId(), result.memberId(), result.authorId());
  }
}
