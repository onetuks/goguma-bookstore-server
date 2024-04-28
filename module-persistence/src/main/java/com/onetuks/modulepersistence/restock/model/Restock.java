package com.onetuks.modulepersistence.restock.model;

import com.onetuks.modulepersistence.book.model.Book;
import com.onetuks.modulepersistence.member.model.Member;
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
    name = "restocks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "book_id"}))
public class Restock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "restock_id", nullable = false)
  private Long restockId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @Builder
  public Restock(Member member, Book book) {
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
    Restock restock = (Restock) o;
    return Objects.equals(restockId, restock.restockId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(restockId);
  }
}
