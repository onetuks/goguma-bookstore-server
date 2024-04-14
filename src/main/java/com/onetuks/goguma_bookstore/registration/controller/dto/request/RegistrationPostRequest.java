package com.onetuks.goguma_bookstore.registration.controller.dto.request;

import com.onetuks.goguma_bookstore.registration.service.dto.param.RegistrationPostParam;

public record RegistrationPostRequest(
    String title,
    String summary,
    Long price,
    Long stockCount,
    String isbn,
    String publisher,
    Boolean promotion) {

  public RegistrationPostParam to() {
    return new RegistrationPostParam(
        title(), summary(), price(), stockCount(), isbn(), publisher(), promotion());
  }
}
