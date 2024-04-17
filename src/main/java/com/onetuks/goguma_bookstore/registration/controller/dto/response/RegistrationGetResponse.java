package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import java.util.List;

public record RegistrationGetResponse(
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

  public static RegistrationGetResponse from(RegistrationGetResult result) {
    return new RegistrationGetResponse(
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

  public record RegistrationgetResponses(List<RegistrationGetResponse> responses) {

    public static RegistrationgetResponses from(List<RegistrationGetResult> results) {
      return new RegistrationgetResponses(
          results.stream().map(RegistrationGetResponse::from).toList());
    }
  }
}
