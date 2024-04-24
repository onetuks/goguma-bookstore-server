package com.onetuks.goguma_bookstore.book.controller.dto.response;

import com.onetuks.goguma_bookstore.book.model.vo.Category;
import com.onetuks.goguma_bookstore.book.service.dto.result.BookGetResult;
import java.util.List;
import org.springframework.data.domain.Page;

public record BookGetResponse(
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
    List<String> previewUrls,
    long favoriteCount,
    long viewCount,
    long salesCount,
    long reviewCount,
    float reviewScore) {

  public record BookGetResponses(Page<BookGetResponse> responses) {

    public static BookGetResponses from(Page<BookGetResult> results) {
      return new BookGetResponses(results.map(BookGetResponse::from));
    }
  }

  public static BookGetResponse from(BookGetResult result) {
    return new BookGetResponse(
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
        result.previewUrls(),
        result.favoriteCount(),
        result.viewCount(),
        result.salesCount(),
        result.reviewCount(),
        result.reviewScore());
  }
}
