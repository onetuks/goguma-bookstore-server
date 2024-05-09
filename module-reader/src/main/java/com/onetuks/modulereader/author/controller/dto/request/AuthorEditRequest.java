package com.onetuks.modulereader.author.controller.dto.request;

import com.onetuks.modulereader.author.service.dto.param.AuthorEditParam;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record AuthorEditRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotBlank @Length(min = 1, max = 100) String introduction,
    @NotBlank @URL String instagramUrl) {

  public AuthorEditParam to() {
    return new AuthorEditParam(nickname(), introduction(), instagramUrl());
  }
}
