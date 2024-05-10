package com.onetuks.modulereader.book.service.dto.result;

import com.onetuks.modulepersistence.book.entity.BookEntity;
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
    long commentCount) {

  public static BookGetResult from(BookEntity bookEntity) {
    return new BookGetResult(
        bookEntity.getBookId(),
        bookEntity.getAuthorEntity().getAuthorId(),
        bookEntity.getAuthorNickname(),
        bookEntity.getTitle(),
        bookEntity.getOneLiner(),
        bookEntity.getSummary(),
        bookEntity.getCategories(),
        bookEntity.getIsbn(),
        bookEntity.getHeight(),
        bookEntity.getWidth(),
        bookEntity.getCoverType(),
        bookEntity.getPageCount(),
        bookEntity.getRegularPrice(),
        bookEntity.getPurchasePrice(),
        bookEntity.isPromotion(),
        bookEntity.getPublisher(),
        bookEntity.getStockCount(),
        bookEntity.getCoverImgUrl(),
        bookEntity.getDetailImgUrls(),
        bookEntity.getPreviewUrls(),
        bookEntity.getFavoriteCount(),
        bookEntity.getViewCount(),
        bookEntity.getSalesCount(),
        bookEntity.getCommentCount());
  }
}
