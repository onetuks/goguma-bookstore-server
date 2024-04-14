package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationEditResult;

public record RegistrationEditResponse(
    long registrationId,
    String coverImgUrl,
    String title,
    String summary,
    long price,
    long stockCount,
    String isbn,
    String publisher,
    boolean promotion,
    String sampleUrl) {

  public static RegistrationEditResponse from(RegistrationEditResult result) {
    return new RegistrationEditResponse(
        result.registrationId(),
        result.coverImgUrl(),
        result.title(),
        result.summary(),
        result.price(),
        result.stockCount(),
        result.isbn(),
        result.publisher(),
        result.promotion(),
        result.sampleUrl());
  }
}
