package com.onetuks.modulereader.book.service.dto.result;

import com.onetuks.modulereader.registration.service.dto.result.RegistrationInspectionResult;
import com.onetuks.modulereader.registration.service.dto.result.RegistrationResult;
import com.onetuks.modulepersistence.book.vo.Category;
import java.util.List;

public record BookEditResult(
    long bookId,
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
    List<String> previewUrls) {

  public static BookEditResult from(
      long bookId, RegistrationResult result, RegistrationInspectionResult inspection) {
    return new BookEditResult(
        bookId,
        inspection.registrationId(),
        inspection.approvalResult(),
        inspection.approvalMemo(),
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
        result.previewUrls());
  }
}
