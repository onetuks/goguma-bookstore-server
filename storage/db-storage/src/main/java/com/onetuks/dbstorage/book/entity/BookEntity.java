package com.onetuks.dbstorage.book.entity;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.dbstorage.author.entity.AuthorEntity;
import com.onetuks.dbstorage.book.entity.embedded.BookConceptualEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPhysicalInfoEmbedded;
import com.onetuks.dbstorage.book.entity.embedded.BookPriceInfoEmbedded;
import com.onetuks.dbstorage.book.vo.Category;
import com.onetuks.dbstorage.order.vo.CoverImgFilePath;
import com.onetuks.dbstorage.order.vo.DetailImgFilePath.DetailImgFilePaths;
import com.onetuks.dbstorage.order.vo.PreviewFilePath.PreviewFilePaths;
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

  @Embedded private BookConceptualEmbedded bookConceptualEmbedded;

  @Embedded private BookPhysicalInfoEmbedded bookPhysicalInfoEmbedded;

  @Embedded private BookPriceInfoEmbedded bookPriceInfoEmbedded;

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
      BookConceptualEmbedded bookConceptualEmbedded,
      BookPhysicalInfoEmbedded bookPhysicalInfoEmbedded,
      BookPriceInfoEmbedded bookPriceInfoEmbedded,
      String coverImgFilePath,
      List<String> detailImgFilePaths,
      List<String> previewFilePaths) {
    this.authorEntity = authorEntity;
    this.authorNickname = authorNickname;
    this.bookConceptualEmbedded = bookConceptualEmbedded;
    this.bookPhysicalInfoEmbedded = bookPhysicalInfoEmbedded;
    this.bookPriceInfoEmbedded = bookPriceInfoEmbedded;
    this.coverImgFilePath = CoverImgFilePath.of(coverImgFilePath);
    this.detailImgFilePaths = DetailImgFilePaths.of(detailImgFilePaths);
    this.previewFilePaths = PreviewFilePaths.of(previewFilePaths);
    this.bookStaticsEntity = BookStaticsEntity.init(this);
  }

  public String getTitle() {
    return this.bookConceptualEmbedded.getTitle();
  }

  public String getOneLiner() {
    return this.bookConceptualEmbedded.getOneLiner();
  }

  public String getSummary() {
    return this.bookConceptualEmbedded.getSummary();
  }

  public List<Category> getCategories() {
    return this.bookConceptualEmbedded.getCategories();
  }

  public String getPublisher() {
    return this.bookConceptualEmbedded.getPublisher();
  }

  public String getIsbn() {
    return this.bookConceptualEmbedded.getIsbn();
  }

  public int getHeight() {
    return this.bookPhysicalInfoEmbedded.getHeight();
  }

  public int getWidth() {
    return this.bookPhysicalInfoEmbedded.getWidth();
  }

  public String getCoverType() {
    return this.bookPhysicalInfoEmbedded.getCoverType();
  }

  public long getPageCount() {
    return this.bookPhysicalInfoEmbedded.getPageCount();
  }

  public long getRegularPrice() {
    return this.bookPriceInfoEmbedded.getRegularPrice();
  }

  public long getPurchasePrice() {
    return this.bookPriceInfoEmbedded.getPurchasePrice();
  }

  public boolean isPromotion() {
    return this.bookPriceInfoEmbedded.getPromotion();
  }

  public long getStockCount() {
    return this.bookPriceInfoEmbedded.getStockCount();
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
    this.bookPriceInfoEmbedded =
        this.bookPriceInfoEmbedded.changeStockCount(getStockCount() > NO_STOCK ? NO_STOCK : newStockCount);
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
