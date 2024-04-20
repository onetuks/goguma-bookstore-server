package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.model.vo.PageSizeInfo;
import com.onetuks.goguma_bookstore.registration.model.Registration;
import java.util.List;

public record RegistrationCreateResult(
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

  public static RegistrationCreateResult from(Registration registration) {
    return new RegistrationCreateResult(
        registration.getRegistrationId(),
        registration.getApprovalResult(),
        registration.getApprovalMemo(),
        registration.getTitle(),
        registration.getOneLiner(),
        registration.getSummary(),
        registration.getCategories(),
        registration.getPublisher(),
        registration.getIsbn(),
        registration.getPageSizeInfo(),
        registration.getCoverType(),
        registration.getPageCount(),
        registration.getPrice(),
        registration.getStockCount(),
        registration.getPromotion(),
        registration.getCoverImgUrl(),
        registration.getDetailImgUrls(),
        registration.getPreviewUrls(),
        registration.getSampleUrl());
  }
}
