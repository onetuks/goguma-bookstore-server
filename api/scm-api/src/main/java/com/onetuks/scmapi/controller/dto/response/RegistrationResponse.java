package com.onetuks.scmapi.controller.dto.response;

import com.onetuks.dbstorage.book.vo.Category;
import com.onetuks.modulescm.registration.service.dto.result.RegistrationResult;
import java.util.List;
import org.springframework.data.domain.Page;

public record RegistrationResponse(
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

  public static RegistrationResponse from(RegistrationResult result) {
    return new RegistrationResponse(
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

  public record RegistrationResponses(Page<RegistrationResponse> responses) {

    public static RegistrationResponses from(Page<RegistrationResult> results) {
      return new RegistrationResponses(results.map(RegistrationResponse::from));
    }
  }
}
