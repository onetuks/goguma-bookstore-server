package com.onetuks.modulereader.subscribe.controller.dto.request;

import com.onetuks.modulereader.subscribe.service.dto.param.SubscribePostParam;
import jakarta.validation.constraints.Positive;

public record SubscribePostRequest(@Positive Long authorId) {

  public SubscribePostParam to() {
    return new SubscribePostParam(authorId);
  }
}
