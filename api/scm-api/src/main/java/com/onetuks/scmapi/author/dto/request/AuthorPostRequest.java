package com.onetuks.scmapi.author.dto.request;

import com.onetuks.scmdomain.author.param.AuthorCreateParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record AuthorPostRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotBlank @Length(min = 1, max = 100) String introduction,
    @NotBlank @URL String instagramUrl,
    @NotBlank @Length(min = 10, max = 10) @Pattern(regexp = "^\\d+$") String businessNumber,
    @NotBlank @Length(min = 18, max = 18) @Pattern(regexp = "^\\d+$") String mailOrderSalesNumber) {

  public AuthorCreateParam to() {
    return new AuthorCreateParam(
        nickname(), introduction(), instagramUrl(), businessNumber(), mailOrderSalesNumber());
  }
}
