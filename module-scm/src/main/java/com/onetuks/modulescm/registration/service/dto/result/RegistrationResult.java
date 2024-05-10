package com.onetuks.modulescm.registration.service.dto.result;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.registration.entity.RegistrationEntity;
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

  public static RegistrationResult from(RegistrationEntity registrationEntity) {
    return new RegistrationResult(
        registrationEntity.getRegistrationId(),
        registrationEntity.getApprovalResult(),
        registrationEntity.getApprovalMemo(),
        registrationEntity.getTitle(),
        registrationEntity.getOneLiner(),
        registrationEntity.getSummary(),
        registrationEntity.getCategories(),
        registrationEntity.getIsbn(),
        registrationEntity.getHeight(),
        registrationEntity.getWidth(),
        registrationEntity.getCoverType(),
        registrationEntity.getPageCount(),
        registrationEntity.getRegularPrice(),
        registrationEntity.getPurchasePrice(),
        registrationEntity.isPromotion(),
        registrationEntity.getPublisher(),
        registrationEntity.getStockCount(),
        registrationEntity.getCoverImgUrl(),
        registrationEntity.getDetailImgUrls(),
        registrationEntity.getPreviewUrls(),
        registrationEntity.getSampleUrl());
  }
}
