package com.onetuks.goguma_bookstore.author.controller.dto.request;

import com.onetuks.goguma_bookstore.author.service.dto.param.AuthorCreateParam;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record AuthorCreateEnrollmentRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotBlank @Length(min = 1, max = 100) String introduction,
    @NotBlank @URL String instagramUrl) {

  public AuthorCreateParam to() {
    return new AuthorCreateParam(nickname(), introduction(), instagramUrl());
  }
}
