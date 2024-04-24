package com.onetuks.goguma_bookstore.book.model;

import com.onetuks.goguma_bookstore.book.model.converter.CustomFileListToJsonConverter;
import com.onetuks.goguma_bookstore.global.vo.file.ReviewImgFile;
import com.onetuks.goguma_bookstore.member.model.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "reviews",
    uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "member_id"}))
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id", nullable = false)
  private Long reviewId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Column(name = "score", nullable = false)
  private Float score;

  @Column(name = "content", nullable = false)
  private String content;

  @Convert(converter = CustomFileListToJsonConverter.class)
  @Column(name = "review_img_uris")
  private List<ReviewImgFile> reviewImgFiles;

  @Builder
  public Review(
      Book book, Member member, Float score, String content, List<ReviewImgFile> reviewImgFiles) {
    this.book = book;
    this.member = member;
    this.score = score;
    this.content = content;
    this.reviewImgFiles = reviewImgFiles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Review review = (Review) o;
    return Objects.equals(reviewId, review.reviewId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(reviewId);
  }
}
