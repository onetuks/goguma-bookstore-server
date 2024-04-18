package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;

public record RegistrationCreateResponse(
    long registrationId,
    boolean approvalResult,
    String approvalMemo,
    String coverImgUrl,
    String title,
    String summary,
    long price,
    long stockCount,
    String isbn,
    String publisher,
    boolean promotion,
    String sampleUrl) {

  public static RegistrationCreateResponse from(RegistrationCreateResult result) {
    return new RegistrationCreateResponse(
        result.registrationId(),
        result.approvalResult(),
        result.approvalMemo(),
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
