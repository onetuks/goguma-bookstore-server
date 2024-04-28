package com.onetuks.modulepersistence.author.model;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "author_statics")
public class AuthorStatics {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "author_statics_id", nullable = false)
  private Long authorStaticsId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "author_id", unique = true, nullable = false)
  private Author author;

  @Column(name = "subscribe_count", nullable = false)
  private Long subscribeCount;

  @Column(name = "book_count", nullable = false)
  private Long bookCount;

  @Column(name = "restock_count", nullable = false)
  private Long restockCount;

  @Builder
  public AuthorStatics(Author author, Long subscribeCount, Long bookCount, Long restockCount) {
    this.author = author;
    this.subscribeCount = Objects.requireNonNullElse(subscribeCount, 0L);
    this.bookCount = Objects.requireNonNullElse(bookCount, 0L);
    this.restockCount = Objects.requireNonNullElse(restockCount, 0L);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthorStatics that = (AuthorStatics) o;
    return Objects.equals(authorStaticsId, that.authorStaticsId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(authorStaticsId);
  }
}
