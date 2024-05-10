package com.onetuks.readerapi.controller.dto.response;

import com.onetuks.modulereader.subscribe.service.dto.result.SubscribeResult;
import org.springframework.data.domain.Page;

public record SubscribeResponse(long subscribeId, long memberId, long authorId) {

  public static SubscribeResponse from(SubscribeResult result) {
    return new SubscribeResponse(result.subscribeId(), result.memberId(), result.authorId());
  }

  public record SubscribeResponses(Page<SubscribeResponse> responses) {
    public static SubscribeResponses from(Page<SubscribeResult> results) {
      return new SubscribeResponses(results.map(SubscribeResponse::from));
    }
  }
}
