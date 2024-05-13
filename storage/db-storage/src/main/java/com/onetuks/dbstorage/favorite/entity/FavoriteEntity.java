package com.onetuks.dbstorage.favorite.entity;

import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import jakarta.persistence.CascadeType;
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
public class FavoriteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "favorite_id", nullable = false)
  private Long favoriteId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity bookEntity;

  public FavoriteEntity(MemberEntity memberEntity, BookEntity bookEntity) {
    this.memberEntity = memberEntity;
    this.bookEntity = bookEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FavoriteEntity favoriteEntity = (FavoriteEntity) o;
    return Objects.equals(favoriteId, favoriteEntity.favoriteId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(favoriteId);
  }
}
