package com.onetuks.goguma_bookstore.subscribe.model;

import com.onetuks.goguma_bookstore.author.model.Author;
import com.onetuks.goguma_bookstore.member.model.Member;
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
    name = "subscribes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "author_id"}))
public class Subscribe {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscribe_id", nullable = false)
  private Long subscribeId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "author_id")
  private Author author;

  @Builder
  public Subscribe(Member member, Author author) {
    this.member = member;
    this.author = author;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Subscribe subscribe = (Subscribe) o;
    return Objects.equals(subscribeId, subscribe.subscribeId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(subscribeId);
  }
}
