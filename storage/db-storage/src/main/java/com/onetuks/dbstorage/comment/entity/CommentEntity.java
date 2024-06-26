package com.onetuks.dbstorage.comment.entity;

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
    name = "comments",
    uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "member_id"}))
public class CommentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id", nullable = false)
  private Long commentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity bookEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity memberEntity;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  public CommentEntity(
      Long commentId,
      BookEntity bookEntity,
      MemberEntity memberEntity,
      String title,
      String content) {
    this.commentId = commentId;
    this.bookEntity = bookEntity;
    this.memberEntity = memberEntity;
    this.title = title;
    this.content = content;
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
    CommentEntity commentEntity = (CommentEntity) o;
    return Objects.equals(commentId, commentEntity.commentId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(commentId);
  }
}
