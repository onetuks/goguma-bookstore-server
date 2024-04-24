package com.onetuks.goguma_bookstore.registration.service.dto.result;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.registration.model.Registration;
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
        registration.getApprovalInfo().getApprovalResult(),
        registration.getApprovalInfo().getApprovalMemo(),
        registration.getBookConceptualInfo().getTitle(),
        registration.getBookConceptualInfo().getOneLiner(),
        registration.getBookConceptualInfo().getSummary(),
        registration.getBookConceptualInfo().getCategories(),
        registration.getBookConceptualInfo().getIsbn(),
        registration.getBookPhysicalInfo().getHeight(),
        registration.getBookPhysicalInfo().getWidth(),
        registration.getBookPhysicalInfo().getCoverType(),
        registration.getBookPhysicalInfo().getPageCount(),
        registration.getBookPriceInfo().getRegularPrice(),
        registration.getBookPriceInfo().getPurchasePrice(),
        registration.getBookPriceInfo().getPromotion(),
        registration.getPublisher(),
        registration.getStockCount(),
        registration.getCoverImgUrl(),
        registration.getDetailImgUrls(),
        registration.getPreviewUrls(),
        registration.getSampleUrl());
  }
}
