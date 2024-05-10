package com.onetuks.scmapi.controller.dto.response;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulescm.book.service.dto.result.BookEditResult;
import java.util.List;

public record BookEditResponse(
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

  public static BookEditResponse from(BookEditResult result) {
    return new BookEditResponse(
        result.bookId(),
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
        result.previewUrls());
  }
}
