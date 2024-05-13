package com.onetuks.dbstorage.book.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "book_id", nullable = false, unique = true)
  private BookEntity bookEntity;

  @Column(name = "favorite_count", nullable = false)
  private Long favoriteCount;

  @Column(name = "view_count", nullable = false)
  private Long viewCount;

  @Column(name = "sales_count", nullable = false)
  private Long salesCount;

  @Column(name = "comment_count", nullable = false)
  private Long commentCount;

  public BookStaticsEntity(
      BookEntity bookEntity,
      Long favoriteCount,
      Long viewCount,
      Long salesCount,
      Long commentCount) {
    this.bookEntity = bookEntity;
    this.favoriteCount = favoriteCount;
    this.viewCount = viewCount;
    this.salesCount = salesCount;
    this.commentCount = commentCount;
  }

  public static BookStaticsEntity init(BookEntity bookEntity) {
    return new BookStaticsEntity(bookEntity, 0L, 0L, 0L, 0L);
  }

  @Override
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
  public int hashCode() {
    return Objects.hashCode(bookStaticsId);
  }
}
