package com.onetuks.modulepersistence.book.entity;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.modulepersistence.author.entity.AuthorEntity;
import com.onetuks.modulepersistence.book.entity.embedded.BookConceptualInfo;
import com.onetuks.modulepersistence.book.entity.embedded.BookPhysicalInfo;
import com.onetuks.modulepersistence.book.entity.embedded.BookPriceInfo;
import com.onetuks.modulepersistence.book.vo.Category;
import com.onetuks.modulepersistence.global.vo.file.CoverImgFilePath;
import com.onetuks.modulepersistence.global.vo.file.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.modulepersistence.global.vo.file.PreviewFilePath.PreviewFilePaths;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class BookEntity {

  private static final long NO_STOCK = 0L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id", nullable = false)
  private Long bookId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = REMOVE)
  @JoinColumn(name = "author_id", nullable = false)
  private AuthorEntity authorEntity;

  @Column(name = "author_nickname", nullable = false)
  private String authorNickname;

  @Embedded private BookConceptualInfo bookConceptualInfo;

  @Embedded private BookPhysicalInfo bookPhysicalInfo;

  @Embedded private BookPriceInfo bookPriceInfo;

  @Embedded private CoverImgFilePath coverImgFilePath;

  @Embedded private DetailImgFilePaths detailImgFilePaths;

  @Embedded private PreviewFilePaths previewFilePaths;

  @OneToOne(
      mappedBy = "book",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private BookStaticsEntity bookStaticsEntity;

  @Builder
  public BookEntity(
      AuthorEntity authorEntity,
      String authorNickname,
      BookConceptualInfo bookConceptualInfo,
      BookPhysicalInfo bookPhysicalInfo,
      BookPriceInfo bookPriceInfo,
      String coverImgFilePath,
      List<String> detailImgFilePaths,
      List<String> previewFilePaths) {
    this.authorEntity = authorEntity;
    this.authorNickname = authorNickname;
    this.bookConceptualInfo = bookConceptualInfo;
    this.bookPhysicalInfo = bookPhysicalInfo;
    this.bookPriceInfo = bookPriceInfo;
    this.coverImgFilePath = CoverImgFilePath.of(coverImgFilePath);
    this.detailImgFilePaths = DetailImgFilePaths.of(detailImgFilePaths);
    this.previewFilePaths = PreviewFilePaths.of(previewFilePaths);
    this.bookStaticsEntity = BookStaticsEntity.init(this);
  }

  public String getTitle() {
    return this.bookConceptualInfo.getTitle();
  }

  public String getOneLiner() {
    return this.bookConceptualInfo.getOneLiner();
  }

  public String getSummary() {
    return this.bookConceptualInfo.getSummary();
  }

  public List<Category> getCategories() {
    return this.bookConceptualInfo.getCategories();
  }

  public String getPublisher() {
    return this.bookConceptualInfo.getPublisher();
  }

  public String getIsbn() {
    return this.bookConceptualInfo.getIsbn();
  }

  public int getHeight() {
    return this.bookPhysicalInfo.getHeight();
  }

  public int getWidth() {
    return this.bookPhysicalInfo.getWidth();
  }

  public String getCoverType() {
    return this.bookPhysicalInfo.getCoverType();
  }

  public long getPageCount() {
    return this.bookPhysicalInfo.getPageCount();
  }

  public long getRegularPrice() {
    return this.bookPriceInfo.getRegularPrice();
  }

  public long getPurchasePrice() {
    return this.bookPriceInfo.getPurchasePrice();
  }

  public boolean isPromotion() {
    return this.bookPriceInfo.getPromotion();
  }

  public long getStockCount() {
    return this.bookPriceInfo.getStockCount();
  }

  public String getCoverImgUrl() {
    return this.coverImgFilePath.getUrl();
  }

  public List<String> getDetailImgUrls() {
    return detailImgFilePaths.getUrls();
  }

  public List<String> getPreviewUrls() {
    return previewFilePaths.getUrls();
  }

  public long getFavoriteCount() {
    return this.bookStaticsEntity.getFavoriteCount();
  }

  public long getViewCount() {
    return this.bookStaticsEntity.getViewCount();
  }

  public long getSalesCount() {
    return this.bookStaticsEntity.getSalesCount();
  }

  public long getCommentCount() {
    return this.bookStaticsEntity.getCommentCount();
  }

  public BookEntity changeStockCount(long newStockCount) {
    this.bookPriceInfo =
        this.bookPriceInfo.changeStockCount(getStockCount() > NO_STOCK ? NO_STOCK : newStockCount);
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
    BookEntity bookEntity = (BookEntity) o;
    return Objects.equals(bookId, bookEntity.bookId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(bookId);
  }
}
