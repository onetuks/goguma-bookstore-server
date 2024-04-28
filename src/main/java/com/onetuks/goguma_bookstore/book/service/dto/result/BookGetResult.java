package com.onetuks.goguma_bookstore.book.service.dto.result;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.vo.Category;
import java.util.List;

public record BookGetResult(
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

  public static BookGetResult from(Book book) {
    return new BookGetResult(
        book.getBookId(),
        book.getAuthor().getAuthorId(),
        book.getAuthorNickname(),
        book.getTitle(),
        book.getOneLiner(),
        book.getSummary(),
        book.getCategories(),
        book.getIsbn(),
        book.getHeight(),
        book.getWidth(),
        book.getCoverType(),
        book.getPageCount(),
        book.getRegularPrice(),
        book.getPurchasePrice(),
        book.isPromotion(),
        book.getPublisher(),
        book.getStockCount(),
        book.getCoverImgUrl(),
        book.getDetailImgUrls(),
        book.getPreviewUrls(),
        book.getFavoriteCount(),
        book.getViewCount(),
        book.getSalesCount(),
        book.getReviewCount(),
        book.getReviewScore());
  }
}
