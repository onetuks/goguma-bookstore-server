package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.registration.model.Registration;
import java.util.List;

public record RegistrationResult(
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

  public static RegistrationResult from(Registration registration) {
    return new RegistrationResult(
        registration.getRegistrationId(),
        registration.getApprovalResult(),
        registration.getApprovalMemo(),
        registration.getTitle(),
        registration.getOneLiner(),
        registration.getSummary(),
        registration.getCategories(),
        registration.getIsbn(),
        registration.getHeight(),
        registration.getWidth(),
        registration.getCoverType(),
        registration.getPageCount(),
        registration.getRegularPrice(),
        registration.getPurchasePrice(),
        registration.isPromotion(),
        registration.getPublisher(),
        registration.getStockCount(),
        registration.getCoverImgUrl(),
        registration.getDetailImgUrls(),
        registration.getPreviewUrls(),
        registration.getSampleUrl());
  }
}
