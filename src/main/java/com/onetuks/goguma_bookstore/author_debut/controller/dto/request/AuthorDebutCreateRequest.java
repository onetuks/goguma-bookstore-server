package com.onetuks.goguma_bookstore.author_debut.controller.dto.request;

import com.onetuks.goguma_bookstore.author_debut.service.dto.param.AuthorDebutCreateParam;
import jakarta.validation.constraints.NotBlank;

public record AuthorDebutCreateRequest(@NotBlank String nickname, @NotBlank String introduction) {

  public AuthorDebutCreateParam to() {
    return new AuthorDebutCreateParam(this.nickname, this.introduction);
  }
}
