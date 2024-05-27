package com.onetuks.dbstorage.book.converter;

import com.onetuks.coredomain.book.model.Book;
import com.onetuks.coredomain.book.model.BookStatics;
import com.onetuks.coredomain.book.model.vo.BookConceptualInfo;
import com.onetuks.coredomain.book.model.vo.BookPhysicalInfo;
import com.onetuks.coredomain.book.model.vo.BookPriceInfo;
import com.onetuks.coredomain.global.file.filepath.CoverImgFilePath;
import com.onetuks.coredomain.global.file.filepath.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.coredomain.global.file.filepath.PreviewFilePath.PreviewFilePaths;
import com.onetuks.coredomain.registration.model.Registration;
import com.onetuks.dbstorage.author.converter.AuthorConverter;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.book.entity.BookStaticsEntity;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

  private final AuthorConverter authorConverter;

  public BookConverter(AuthorConverter authorConverter) {
    this.authorConverter = authorConverter;
  }

  public BookEntity toEntity(Book book) {
    return new BookEntity(
        book.bookId(),
        authorConverter.toEntity(book.author()),
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
        book.coverImgFilePath().getUri(),
        book.detailImgFilePaths().getUris(),
        book.previewFilePaths().getUris(),
        toEntity(book.bookStatics()));
  }

  public BookEntity toEntity(Registration registration) {
    return new BookEntity(
        null,
        authorConverter.toEntity(registration.author()),
        registration.author().nickname().nicknameValue(),
        registration.bookConceptualInfo().title(),
        registration.bookConceptualInfo().oneLiner(),
        registration.bookConceptualInfo().summary(),
        registration.bookConceptualInfo().categories(),
        registration.bookConceptualInfo().publisher(),
        registration.bookConceptualInfo().isbn(),
        registration.bookPhysicalInfo().height(),
        registration.bookPhysicalInfo().width(),
        registration.bookPhysicalInfo().coverType(),
        registration.bookPhysicalInfo().pageCount(),
        registration.bookPriceInfo().price(),
        registration.bookPriceInfo().salesRate(),
        registration.bookPriceInfo().isPromotion(),
        registration.bookPriceInfo().stockCount(),
        registration.coverImgFilePath().getUri(),
        registration.detailImgFilePaths().getUris(),
        registration.previewFilePaths().getUris(),
        null);
  }

  public Book toDomain(BookEntity bookEntity) {
    return new Book(
        bookEntity.getBookId(),
        authorConverter.toDomain(bookEntity.getAuthorEntity()),
        new BookConceptualInfo(
            bookEntity.getTitle(),
            bookEntity.getOneLiner(),
            bookEntity.getSummary(),
            bookEntity.getCategories(),
            bookEntity.getPublisher(),
            bookEntity.getIsbn()),
        new BookPhysicalInfo(
            bookEntity.getHeight(),
            bookEntity.getWidth(),
            bookEntity.getCoverType(),
            bookEntity.getPageCount()),
        new BookPriceInfo(
            bookEntity.getPrice(),
            bookEntity.getSalesRate(),
            bookEntity.getIsPromotion(),
            bookEntity.getStockCount()),
        CoverImgFilePath.of(bookEntity.getCoverImgUri()),
        DetailImgFilePaths.of(bookEntity.getDetailImgUris()),
        PreviewFilePaths.of(bookEntity.getPreviewUris()),
        toDomain(bookEntity.getBookStaticsEntity()));
  }

  private BookStaticsEntity toEntity(BookStatics bookStatics) {
    return new BookStaticsEntity(
        bookStatics.bookStaticsId(),
        bookStatics.favoriteCount(),
        bookStatics.viewCount(),
        bookStatics.salesCount(),
        bookStatics.commentCount(),
        bookStatics.restockCount(),
        bookStatics.reviewScore());
  }

  public BookStatics toDomain(BookStaticsEntity bookStaticsEntity) {
    return new BookStatics(
        bookStaticsEntity.getBookStaticsId(),
        bookStaticsEntity.getFavoriteCount(),
        bookStaticsEntity.getViewCount(),
        bookStaticsEntity.getSalesCount(),
        bookStaticsEntity.getCommentCount(),
        bookStaticsEntity.getRestockCount(),
        bookStaticsEntity.getReviewScore());
  }
}
