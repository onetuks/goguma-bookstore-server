package com.onetuks.modulereader.book.service.dto.result;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.book.vo.Category;
import java.util.List;

public record BookResult(
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

  public static BookResult from(Book book) {
    return new BookResult(
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
        book.getPreviewUrls());
  }
}
