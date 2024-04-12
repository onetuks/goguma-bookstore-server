package com.onetuks.goguma_bookstore.author.controller.dto.request;

import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorEditParam;

public record AuthorEditRequest(String nickname, String introduction) {

  public AuthorEditParam to() {
    return new AuthorEditParam(nickname(), introduction());
  }
}
