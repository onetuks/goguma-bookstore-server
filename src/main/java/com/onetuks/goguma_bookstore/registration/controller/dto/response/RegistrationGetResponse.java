package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationGetResult;
import java.util.List;
import org.springframework.data.domain.Page;

public record RegistrationGetResponse(
    long registrationId,
    boolean approvalResult,
    String approvalMemo,
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String isbn,
    int height,
    int width,
    String coverType,
    long pageCount,
    long regularPrice,
    long purchasePrice,
    boolean promotion,
    String publisher,
    long stockCount,
    String coverImgUrl,
    List<String> detailImgUrls,
    List<String> previewUrls,
    String sampleUrl) {

  public static RegistrationGetResponse from(RegistrationGetResult result) {
    return new RegistrationGetResponse(
        result.registrationId(),
        result.approvalResult(),
        result.approvalMemo(),
        result.title(),
        result.oneLiner(),
        result.summary(),
        result.categories(),
        result.isbn(),
        result.height(),
        result.width(),
        result.coverType(),
        result.pageCount(),
        result.regularPrice(),
        result.purchasePrice(),
        result.promotion(),
        result.publisher(),
        result.stockCount(),
        result.coverImgUrl(),
        result.detailImgUrls(),
        result.previewUrls(),
        result.sampleUrl());
  }

  public record RegistrationGetResponses(Page<RegistrationGetResponse> responses) {

    public static RegistrationGetResponses from(Page<RegistrationGetResult> results) {
      return new RegistrationGetResponses(results.map(RegistrationGetResponse::from));
    }
  }
}
