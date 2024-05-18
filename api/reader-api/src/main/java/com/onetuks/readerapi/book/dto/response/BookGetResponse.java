package com.onetuks.readerapi.book.dto.response;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.book.Category;
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
    String publisher,
    String isbn,
    int height,
    int width,
    String coverType,
    long pageCount,
    long price,
    long salesRate,
    boolean isPromotion,
    long stockCount,
    String coverImgUrl,
    List<String> detailImgUrls,
    List<String> previewUrls,
    long favoriteCount,
    long viewCount,
    long salesCount,
    long commentCount) {

  public record BookGetResponses(Page<BookGetResponse> responses) {

    public static BookGetResponses from(Page<Book> results) {
      return new BookGetResponses(results.map(BookGetResponse::from));
    }
  }

  public static BookGetResponse from(Book book) {
    return new BookGetResponse(
        book.bookId(),
        book.author().authorId(),
        book.author().nickname().nicknameValue(),
        book.bookConceptualInfo().title(),
        book.bookConceptualInfo().oneLiner(),
        book.bookConceptualInfo().summary(),
        book.bookConceptualInfo().categories(),
        book.bookConceptualInfo().publisher(),
        book.bookConceptualInfo().isbn(),
        book.bookPhysicalInfo().height(),
        book.bookPhysicalInfo().width(),
        book.bookPhysicalInfo().coverType(),
        book.bookPhysicalInfo().pageCount(),
        book.bookPriceInfo().price(),
        book.bookPriceInfo().salesRate(),
        book.bookPriceInfo().isPromotion(),
        book.bookPriceInfo().stockCount(),
        book.coverImgFilePath().getUrl(),
        book.detailImgFilePaths().getUrls(),
        book.previewFilePaths().getUrls(),
        book.bookStatics().favoriteCount(),
        book.bookStatics().viewCount(),
        book.bookStatics().salesCount(),
        book.bookStatics().commentCount()
    );
  }
}
