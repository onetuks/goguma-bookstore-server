package com.onetuks.scmapi.controller.dto.response;

import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulescm.book.service.dto.result.BookResult;
import java.util.List;
import org.springframework.data.domain.Page;

public record BookResponse(
    long bookId,
    long authorId,
    String authorNickname,
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

  public record BookResponses(Page<BookResponse> responses) {

    public static BookResponses from(Page<BookResult> results) {
      return new BookResponses(results.map(BookResponse::from));
    }
  }

  public static BookResponse from(BookResult result) {
    return new BookResponse(
        result.bookId(),
        result.authorId(),
        result.authorNickname(),
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
