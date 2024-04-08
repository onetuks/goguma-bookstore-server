package com.onetuks.goguma_bookstore.book.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.author.model.Author;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "book_id", nullable = false)
  private Long bookId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @Column(name = "author_nickname", nullable = false)
  private String authorNickname;

  @Column(name = "cover_img_uri", nullable = false)
  private String coverImgUri;

  @Column(name = "title", nullable = false)
  private String title;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "category", nullable = false)
  private Category category;

  @Column(name = "summary", nullable = false)
  private String summary;

  @Column(name = "price", nullable = false)
  private Long price;

  @Column(name = "stock_count", nullable = false)
  private Long stockCount;

  @Column(name = "isbn", nullable = false)
  private String isbn;

  @Column(name = "publisher", nullable = false)
  private String publisher;

  @Column(name = "promotion", nullable = false)
  private Boolean promotion;

  @OneToOne(
      mappedBy = "book",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private BookStatics bookStatics;

  @Builder
  public Book(
      Author author,
      String authorNickname,
      String coverImgUri,
      String title,
      Category category,
      String summary,
      Long price,
      Long stockCount,
      String isbn,
      String publisher,
      Boolean promotion) {
    this.author = author;
    this.authorNickname = authorNickname;
    this.coverImgUri = coverImgUri;
    this.title = title;
    this.category = category;
    this.summary = summary;
    this.price = price;
    this.stockCount = stockCount;
    this.isbn = isbn;
    this.publisher = publisher;
    this.promotion = promotion;
    this.bookStatics = BookStatics.builder().book(this).build();
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
