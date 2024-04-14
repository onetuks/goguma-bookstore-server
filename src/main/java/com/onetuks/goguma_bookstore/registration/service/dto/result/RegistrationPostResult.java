package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.goguma_bookstore.registration.model.Registration;

public record RegistrationPostResult(
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

  public static RegistrationPostResult from(Registration registration) {
    return new RegistrationPostResult(
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
