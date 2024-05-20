package com.onetuks.dbstorage.author.entity;

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
@Table(name = "author_statics")
public class AuthorStaticsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "author_statics_id", nullable = false)
  private Long authorStaticsId;

  @Column(name = "subscribe_count", nullable = false)
  private Long subscribeCount;

  @Column(name = "book_count", nullable = false)
  private Long bookCount;

  public AuthorStaticsEntity(
      Long authorStaticsId, Long subscribeCount, Long bookCount) {
    this.authorStaticsId = authorStaticsId;
    this.subscribeCount = Objects.requireNonNullElse(subscribeCount, 0L);
    this.bookCount = Objects.requireNonNullElse(bookCount, 0L);
  }

  public static AuthorStaticsEntity init() {
    return new AuthorStaticsEntity(null, 0L, 0L);
  }

  public AuthorStaticsEntity increaseSubscriberCount() {
    this.subscribeCount++;
    return this;
  }

  public AuthorStaticsEntity decreaseSubscriberCount() {
    this.subscribeCount--;
    return this;
  }

  public AuthorStaticsEntity increaseBookCount() {
    this.bookCount++;
    return this;
  }

  public AuthorStaticsEntity decreaseBookCount() {
    this.bookCount--;
    return this;
  }
}
