package com.onetuks.dbstorage.book.entity;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.coreobj.enums.book.Category;
import com.onetuks.dbstorage.author.entity.AuthorEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private AuthorEntity authorEntity;

  @Column(name = "author_nickname", nullable = false)
  private String authorNickname;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "one_liner", nullable = false)
  private String oneLiner;

  @Column(name = "summary", nullable = false)
  private String summary;

  @Type(JsonType.class)
  @Column(name = "categories", nullable = false)
  private List<Category> categories;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "isbn", nullable = false, unique = true)
  private String isbn;

  @Column(name = "height", nullable = false)
  private Integer height;

  @Column(name = "width", nullable = false)
  private Integer width;

  @Column(name = "cover_type", nullable = false)
  private String coverType;

  @Column(name = "page_count", nullable = false)
  private Long pageCount;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "sales_rate", nullable = false)
  private Integer salesRate;

  @Column(name = "is_promotion", nullable = false)
  private Boolean isPromotion;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Column(name = "cover_img_uri")
  private String coverImgUri;

  @Type(JsonType.class)
  @Column(name = "detail_img_uris")
  private List<String> detailImgUris;

  @Type(JsonType.class)
  @Column(name = "preview_uris")
  private List<String> previewUris;

  @OneToOne(
      mappedBy = "book",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private BookStaticsEntity bookStaticsEntity;

  public BookEntity(
      Long bookId,
      AuthorEntity authorEntity,
      String authorNickname,
      String title,
      String oneLiner,
      String summary,
      List<Category> categories,
      String publisher,
      String isbn,
      Integer height,
      Integer width,
      String coverType,
      Long pageCount,
      Long price,
      Integer salesRate,
      Boolean isPromotion,
      Long stockCount,
      String coverImgUri,
      List<String> detailImgUris,
      List<String> previewUris,
      BookStaticsEntity bookStaticsEntity) {
    this.bookId = bookId;
    this.authorEntity = authorEntity;
    this.authorNickname = authorNickname;
    this.title = title;
    this.oneLiner = oneLiner;
    this.summary = summary;
    this.categories = categories;
    this.publisher = publisher;
    this.isbn = isbn;
    this.height = height;
    this.width = width;
    this.coverType = coverType;
    this.pageCount = pageCount;
    this.price = price;
    this.salesRate = salesRate;
    this.isPromotion = isPromotion;
    this.stockCount = stockCount;
    this.coverImgUri = coverImgUri;
    this.detailImgUris = detailImgUris;
    this.previewUris = previewUris;
    this.bookStaticsEntity =
        bookStaticsEntity != null ? bookStaticsEntity : BookStaticsEntity.init(this);
  }

  public BookEntity increaseViewCount() {
    this.bookStaticsEntity = bookStaticsEntity.increaseViewCount();
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
