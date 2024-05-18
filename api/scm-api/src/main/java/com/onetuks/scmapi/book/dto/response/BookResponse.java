package com.onetuks.scmapi.book.dto.response;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coreobj.enums.book.Category;
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
    String publisher,
    String isbn,
    int height,
    int width,
    String coverType,
    long pageCount,
    long regularPrice,
    long purchasePrice,
    boolean promotion,
    long stockCount,
    String coverImgUrl,
    List<String> detailImgUrls,
    List<String> previewUrls) {

  public static BookResponse from(Book book) {
    return new BookResponse(
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
        book.previewFilePaths().getUrls()
    );
  }

  public record BookResponses(Page<BookResponse> responses) {

    public static BookResponses from(Page<Book> results) {
      return new BookResponses(results.map(BookResponse::from));
    }
  }
}
