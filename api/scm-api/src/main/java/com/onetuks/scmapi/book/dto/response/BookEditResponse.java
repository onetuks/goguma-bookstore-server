package com.onetuks.scmapi.book.dto.response;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.book.Category;
import java.util.List;

public record BookEditResponse(
    long bookId,
    String oneLiner,
    String summary,
    List<Category> categories,
    long price,
    int salesRate,
    boolean isPromotion,
    long stockCount,
    String coverImgUrl,
    List<String> detailImgUrls,
    List<String> previewUrls) {

  public static BookEditResponse from(Book book) {
    return new BookEditResponse(
        book.bookId(),
        book.bookConceptualInfo().oneLiner(),
        book.bookConceptualInfo().summary(),
        book.bookConceptualInfo().categories(),
        book.bookPriceInfo().price(),
        book.bookPriceInfo().salesRate(),
        book.bookPriceInfo().isPromotion(),
        book.bookPriceInfo().stockCount(),
        book.coverImgFilePath().getUrl(),
        book.detailImgFilePaths().getUrls(),
        book.previewFilePaths().getUrls()
    );
  }
}
