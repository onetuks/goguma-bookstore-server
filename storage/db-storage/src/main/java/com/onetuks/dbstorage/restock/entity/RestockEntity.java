package com.onetuks.dbstorage.restock.entity;

import com.onetuks.coreobj.annotation.Generated;
import com.onetuks.dbstorage.book.entity.BookEntity;
import com.onetuks.dbstorage.member.entity.MemberEntity;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "restocks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "book_id"}))
public class RestockEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "restock_id", nullable = false)
  private Long restockId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity bookEntity;

  @Column(name = "is_fulfilled", nullable = false)
  private Boolean isFulfilled;

  @Column(name = "is_alarm_permitted", nullable = false)
  private Boolean isAlarmPermitted;

  public RestockEntity(
      Long restockId,
      MemberEntity memberEntity,
      BookEntity bookEntity,
      Boolean isFulfilled,
      Boolean isAlarmPermitted) {
    this.restockId = restockId;
    this.memberEntity = memberEntity;
    this.bookEntity = bookEntity;
    this.isFulfilled = Objects.requireNonNullElse(isFulfilled, false);
    this.isAlarmPermitted =
        Objects.requireNonNullElse(isAlarmPermitted, memberEntity.getIsAlarmPermitted());
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestockEntity restockEntity = (RestockEntity) o;
    return Objects.equals(restockId, restockEntity.restockId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(restockId);
  }
}
