package com.onetuks.scmapi.author.dto.request;

import com.onetuks.scmdomain.author.param.AuthorEditParam;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record AuthorPatchRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotBlank @Length(min = 1, max = 100) String introduction,
    @NotBlank @URL String instagramUrl) {

  public AuthorEditParam to() {
    return new AuthorEditParam(nickname(), introduction(), instagramUrl());
  }
}
