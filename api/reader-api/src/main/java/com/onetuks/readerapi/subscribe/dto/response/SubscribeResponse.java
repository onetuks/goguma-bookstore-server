package com.onetuks.readerapi.subscribe.dto.response;

import com.onetuks.coredomain.subscribe.model.Subscribe;
import org.springframework.data.domain.Page;

public record SubscribeResponse(long subscribeId, long memberId, long authorId) {

  public static SubscribeResponse from(Subscribe subscribe) {
    return new SubscribeResponse(
        subscribe.subscribeId(),
        subscribe.member().memberId(),
        subscribe.author().authorId()
    );
  }

  public record SubscribeResponses(Page<SubscribeResponse> responses) {

    public static SubscribeResponses from(Page<Subscribe> results) {
      return new SubscribeResponses(results.map(SubscribeResponse::from));
    }
  }
}
