package com.onetuks.goguma_bookstore.book.service.dto.result;

import com.onetuks.goguma_bookstore.book.model.Book;
import com.onetuks.goguma_bookstore.book.model.vo.Category;
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
        book.getBookConceptualInfo().getTitle(),
        book.getBookConceptualInfo().getOneLiner(),
        book.getBookConceptualInfo().getSummary(),
        book.getBookConceptualInfo().getCategories(),
        book.getBookConceptualInfo().getIsbn(),
        book.getBookPhysicalInfo().getHeight(),
        book.getBookPhysicalInfo().getWidth(),
        book.getBookPhysicalInfo().getCoverType(),
        book.getBookPhysicalInfo().getPageCount(),
        book.getBookPriceInfo().getRegularPrice(),
        book.getBookPriceInfo().getPurchasePrice(),
        book.getBookPriceInfo().getPromotion(),
        book.getPublisher(),
        book.getStockCount(),
        book.getCoverImgFile().getCoverImgUrl(),
        book.getDetailImgUrls(),
        book.getPreviewUrls());
  }
}
