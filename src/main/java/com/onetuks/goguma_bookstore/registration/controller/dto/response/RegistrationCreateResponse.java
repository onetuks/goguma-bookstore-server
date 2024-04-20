package com.onetuks.goguma_bookstore.registration.controller.dto.response;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
import com.onetuks.goguma_bookstore.registration.service.dto.result.RegistrationCreateResult;
import java.util.List;

public record RegistrationCreateResponse(
    long registrationId,
    boolean approvalResult,
    String approvalMemo,
    String title,
    String oneLiner,
    String summary,
    List<Category> categories,
    String publisher,
    String isbn,
    PageSizeInfo pageSizeInfo,
    String coverType,
    long pageCount,
    long price,
    long stockCount,
    boolean promotion,
    String coverImgUrl,
    List<String> detailImgUrls,
    List<String> previewUrls,
    String sampleUrl) {

  public static RegistrationCreateResponse from(RegistrationCreateResult result) {
    return new RegistrationCreateResponse(
        result.registrationId(),
        result.approvalResult(),
        result.approvalMemo(),
        result.title(),
        result.oneLiner(),
        result.summary(),
        result.categories(),
        result.publisher(),
        result.isbn(),
        result.pageSizeInfo(),
        result.coverType(),
        result.pageCount(),
        result.price(),
        result.stockCount(),
        result.promotion(),
        result.coverImgUrl(),
        result.detailImgUrls(),
        result.previewUrls(),
        result.sampleUrl());
  }
}
