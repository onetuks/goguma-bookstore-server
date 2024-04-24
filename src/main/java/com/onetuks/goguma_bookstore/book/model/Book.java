package com.onetuks.goguma_bookstore.book.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.book.model.converter.CustomFileListToJsonConverter;
import com.onetuks.goguma_bookstore.book.model.embedded.BookConceptualInfo;
import com.onetuks.goguma_bookstore.book.model.embedded.BookPhysicalInfo;
import com.onetuks.goguma_bookstore.book.model.embedded.BookPriceInfo;
import com.onetuks.goguma_bookstore.global.vo.file.CoverImgFile;
import com.onetuks.goguma_bookstore.global.vo.file.DetailImgFile;
import com.onetuks.goguma_bookstore.global.vo.file.PreviewFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "books")
public class Book {

  private static final long NO_STOCK = 0L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id", nullable = false)
  private Long bookId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @Column(name = "author_nickname", nullable = false)
  private String authorNickname;

  @Embedded private BookConceptualInfo bookConceptualInfo;

  @Embedded private BookPhysicalInfo bookPhysicalInfo;

  @Embedded private BookPriceInfo bookPriceInfo;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Embedded private CoverImgFile coverImgFile;

  @Convert(converter = CustomFileListToJsonConverter.class)
  @Column(name = "detail_img_uris", nullable = false)
  private List<DetailImgFile> detailImgFiles;

  @Convert(converter = CustomFileListToJsonConverter.class)
  @Column(name = "preview_uris", nullable = false)
  private List<PreviewFile> previewFiles;

  @OneToOne(
      mappedBy = "book",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private BookStatics bookStatics;

  @Builder
  public Book(
      Author author,
      String authorNickname,
      BookConceptualInfo bookConceptualInfo,
      BookPhysicalInfo bookPhysicalInfo,
      BookPriceInfo bookPriceInfo,
      String publisher,
      Long stockCount,
      CoverImgFile coverImgFile,
      List<DetailImgFile> detailImgFiles,
      List<PreviewFile> previewFiles) {
    this.author = author;
    this.authorNickname = authorNickname;
    this.bookConceptualInfo = bookConceptualInfo;
    this.bookPhysicalInfo = bookPhysicalInfo;
    this.bookPriceInfo = bookPriceInfo;
    this.publisher = publisher;
    this.stockCount = stockCount;
    this.coverImgFile = coverImgFile;
    this.detailImgFiles = detailImgFiles;
    this.previewFiles = previewFiles;
    this.bookStatics = BookStatics.init(this);
  }

  public List<String> getDetailImgUrls() {
    return detailImgFiles.stream().map(DetailImgFile::getDetailImgUrl).toList();
  }

  public List<String> getPreviewUrls() {
    return previewFiles.stream().map(PreviewFile::getPreviewUrl).toList();
  }

  public Book changeStockCount(long newStockCount) {
    this.stockCount = stockCount > NO_STOCK ? NO_STOCK : newStockCount;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return Objects.equals(bookId, book.bookId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bookId);
  }
}
