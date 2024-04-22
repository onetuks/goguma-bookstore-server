package com.onetuks.goguma_bookstore.book.model;

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

  protected BookStatics(Book book, Long favoriteCount, Long viewCount) {
    this.book = book;
    this.favoriteCount = (Long) Objects.requireNonNullElse(favoriteCount, 0);
    this.viewCount = (Long) Objects.requireNonNullElse(viewCount, 0);
  }

  public static BookStatics init(Book book) {
    return new BookStatics(book, 0L, 0L);
  }
}
