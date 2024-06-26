package com.onetuks.dbstorage.book.entity;

import com.onetuks.coreobj.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "book_statics")
public class BookStaticsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_statics_id", nullable = false)
  private Long bookStaticsId;

  @Column(name = "favorite_count", nullable = false)
  private Long favoriteCount;

  @Column(name = "view_count", nullable = false)
  private Long viewCount;

  @Column(name = "sales_count", nullable = false)
  private Long salesCount;

  @Column(name = "comment_count", nullable = false)
  private Long commentCount;

  @Column(name = "restock_count", nullable = false)
  private Long restockCount;

  @Column(name = "review_score", nullable = false)
  private Float reviewScore;

  public BookStaticsEntity(
      Long bookStaticsId,
      Long favoriteCount,
      Long viewCount,
      Long salesCount,
      Long commentCount,
      Long restockCount,
      Float reviewScore) {
    this.bookStaticsId = bookStaticsId;
    this.favoriteCount = favoriteCount;
    this.viewCount = viewCount;
    this.salesCount = salesCount;
    this.commentCount = commentCount;
    this.restockCount = restockCount;
    this.reviewScore = reviewScore;
  }

  public static BookStaticsEntity init() {
    return new BookStaticsEntity(null, 0L, 0L, 0L, 0L, 0L, 0F);
  }

  public BookStaticsEntity increaseFavoriteCount() {
    this.favoriteCount++;
    return this;
  }

  public BookStaticsEntity decreaseFavoriteCount() {
    this.favoriteCount--;
    return this;
  }

  public BookStaticsEntity increaseViewCount() {
    this.viewCount++;
    return this;
  }

  public BookStaticsEntity increaseCommentCount() {
    this.commentCount++;
    return this;
  }

  public BookStaticsEntity decreaseCommentCount() {
    this.commentCount--;
    return this;
  }

  public BookStaticsEntity increaseRestockCount() {
    this.restockCount++;
    return this;
  }

  public BookStaticsEntity decreaseRestockCount() {
    this.restockCount--;
    return this;
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookStaticsEntity that = (BookStaticsEntity) o;
    return Objects.equals(bookStaticsId, that.bookStaticsId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(bookStaticsId);
  }
}
