package com.onetuks.dbstorage.subscribe.entity;

import com.onetuks.dbstorage.author.entity.AuthorEntity;
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
    name = "subscribes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "author_id"}))
public class SubscribeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscribe_id", nullable = false)
  private Long subscribeId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "member_id")
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "author_id")
  private AuthorEntity authorEntity;

  @Builder
  public SubscribeEntity(MemberEntity memberEntity, AuthorEntity authorEntity) {
    this.memberEntity = memberEntity;
    this.authorEntity = authorEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscribeEntity subscribeEntity = (SubscribeEntity) o;
    return Objects.equals(subscribeId, subscribeEntity.subscribeId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(subscribeId);
  }
}
