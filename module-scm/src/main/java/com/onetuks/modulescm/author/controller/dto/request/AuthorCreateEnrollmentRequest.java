package com.onetuks.modulescm.author.controller.dto.request;

import com.onetuks.modulescm.author.service.dto.param.AuthorCreateEnrollmentParam;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record AuthorCreateEnrollmentRequest(
    @NotBlank @Length(min = 1, max = 20) String nickname,
    @NotBlank @Length(min = 1, max = 100) String introduction,
    @NotBlank @URL String instagramUrl,
    @NotBlank @Length(min = 10, max = 10) @Pattern(regexp = "^\\d+$") String businessNumber,
    @NotBlank @Length(min = 18, max = 18) @Pattern(regexp = "^\\d+$") String mailOrderSalesNumber) {

  public AuthorCreateEnrollmentParam to() {
    return new AuthorCreateEnrollmentParam(
        nickname(), introduction(), instagramUrl(), businessNumber(), mailOrderSalesNumber());
  }
}