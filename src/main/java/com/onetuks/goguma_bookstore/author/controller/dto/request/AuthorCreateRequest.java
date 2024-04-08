package com.onetuks.goguma_bookstore.author.controller.dto.request;

import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import jakarta.validation.constraints.NotBlank;

public record AuthorCreateRequest(@NotBlank String nickname, @NotBlank String introduction) {

  public AuthorCreateParam to() {
    return new AuthorCreateParam(this.nickname, this.introduction);
  }
}
