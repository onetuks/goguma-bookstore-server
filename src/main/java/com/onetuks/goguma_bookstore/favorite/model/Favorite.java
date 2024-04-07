package com.onetuks.goguma_bookstore.favorite.model;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.book.model.Book;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "favorites",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "book_id"}))
public class Favorite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "favorite_id", nullable = false)
  private Long favoriteId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @Builder
  public Favorite(Member member, Book book) {
    this.member = member;
    this.book = book;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Favorite favorite = (Favorite) o;
    return Objects.equals(favoriteId, favorite.favoriteId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(favoriteId);
  }
}
