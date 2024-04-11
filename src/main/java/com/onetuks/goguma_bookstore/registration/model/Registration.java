package com.onetuks.goguma_bookstore.registration.model;

import com.onetuks.goguma_bookstore.author.model.Author;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "registrations")
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "registration_id", nullable = false)
  private Long registrationId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private Author author;

  @Column(name = "approval_result", nullable = false)
  private Boolean approvalResult;

  @Column(name = "approval_memo", nullable = false)
  private String approvalMemo;

  @Column(name = "cover_img_uri", nullable = false)
  private String coverImgUri;

  @Column(name = "title", nullable = false)
  private String title;

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

  @Column(name = "sample_uri", nullable = false)
  private String sampleUri;

  @Builder
  public Registration(
      Author author,
      Boolean approvalResult,
      String approvalMemo,
      String coverImgUri,
      String title,
      String summary,
      Long price,
      Long stockCount,
      String isbn,
      String publisher,
      Boolean promotion,
      String sampleUri) {
    this.author = author;
    this.approvalResult = approvalResult;
    this.approvalMemo = approvalMemo;
    this.coverImgUri = coverImgUri;
    this.title = title;
    this.summary = summary;
    this.price = price;
    this.stockCount = stockCount;
    this.isbn = isbn;
    this.publisher = publisher;
    this.promotion = promotion;
    this.sampleUri = sampleUri;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Registration that = (Registration) o;
    return Objects.equals(registrationId, that.registrationId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(registrationId);
  }
}
