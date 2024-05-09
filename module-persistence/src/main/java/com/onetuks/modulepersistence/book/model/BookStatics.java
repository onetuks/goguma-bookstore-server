package com.onetuks.modulepersistence.book.model;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "book_statics")
public class BookStatics {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_statics_id", nullable = false)
  private Long bookStaticsId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "book_id", nullable = false, unique = true)
  private Book book;

  @Column(name = "favorite_count", nullable = false)
  private Long favoriteCount;

  @Column(name = "view_count", nullable = false)
  private Long viewCount;

  @Column(name = "sales_count", nullable = false)
  private Long salesCount;

  @Column(name = "comment_count", nullable = false)
  private Long commentCount;

  public BookStatics(
      Book book, Long favoriteCount, Long viewCount, Long salesCount, Long commentCount) {
    this.book = book;
    this.favoriteCount = favoriteCount;
    this.viewCount = viewCount;
    this.salesCount = salesCount;
    this.commentCount = commentCount;
  }

  public static BookStatics init(Book book) {
    return new BookStatics(book, 0L, 0L, 0L, 0L);
  }

  public void increaseViewCount() {
    this.viewCount++;
  }

  public void increaseFavoriteCount() {
    this.favoriteCount++;
  }

  public void decreaseFavoriteCount() {
    this.favoriteCount--;
  }
}
