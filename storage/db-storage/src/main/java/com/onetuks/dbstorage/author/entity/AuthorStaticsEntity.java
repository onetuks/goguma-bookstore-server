package com.onetuks.dbstorage.author.entity;

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
public class AuthorStaticsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "author_statics_id", nullable = false)
  private Long authorStaticsId;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "author_id", unique = true, nullable = false)
  private AuthorEntity authorEntity;

  @Column(name = "subscribe_count", nullable = false)
  private Long subscribeCount;

  @Column(name = "book_count", nullable = false)
  private Long bookCount;

  @Column(name = "restock_count", nullable = false)
  private Long restockCount;

  @Builder
  public AuthorStaticsEntity(AuthorEntity authorEntity, Long subscribeCount, Long bookCount, Long restockCount) {
    this.authorEntity = authorEntity;
    this.subscribeCount = Objects.requireNonNullElse(subscribeCount, 0L);
    this.bookCount = Objects.requireNonNullElse(bookCount, 0L);
    this.restockCount = Objects.requireNonNullElse(restockCount, 0L);
  }

  public void increaseSubscribeCount() {
    this.subscribeCount++;
  }

  public void decreaseSubscribeCount() {
    this.subscribeCount--;
  }
}
