package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.goguma_bookstore.registration.model.Registration;

public record RegistrationCreateResult(
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

  public static RegistrationCreateResult from(Registration registration) {
    return new RegistrationCreateResult(
        registration.getRegistrationId(),
        registration.getApprovalResult(),
        registration.getApprovalMemo(),
        registration.getCoverImgUrl(),
        registration.getTitle(),
        registration.getSummary(),
        registration.getPrice(),
        registration.getStockCount(),
        registration.getIsbn(),
        registration.getPublisher(),
        registration.getPromotion(),
        registration.getSampleUrl());
  }
}