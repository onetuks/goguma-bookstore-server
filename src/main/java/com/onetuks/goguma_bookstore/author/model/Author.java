package com.onetuks.goguma_bookstore.author.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
import com.onetuks.goguma_bookstore.author.model.vo.ProfileImg;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "authors")
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "author_id", nullable = false)
  private Long authorId;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "member_id", unique = true)
  private Member member;

  @Embedded private ProfileImg profileImg;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "introduction", nullable = false)
  private String introduction;

  @Embedded private EnrollmentInfo enrollmentInfo;

  @OneToOne(
      mappedBy = "author",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private AuthorStatics authorStatics;

  @Builder
  public Author(
      Member member,
      String profileImgUri,
      String nickname,
      String introduction,
      EnrollmentInfo enrollmentInfo) {
    this.member = member;
    this.profileImg = new ProfileImg(profileImgUri);
    this.nickname = nickname;
    this.introduction = introduction;
    this.enrollmentInfo =
        Objects.requireNonNullElse(
            enrollmentInfo,
            EnrollmentInfo.builder()
                .enrollmentPassed(false)
                .enrollmentAt(LocalDateTime.now())
                .build());
    this.authorStatics = AuthorStatics.builder().author(this).build();
  }

  public String getProfileImgUrl() {
    return this.profileImg.getProfileImgUrl();
  }

  public String getEscrowServiceUrl() {
    return this.enrollmentInfo.getEscrowService().getEscrowServiceUrl();
  }

  public String getMailOrderSalesUrl() {
    return this.enrollmentInfo.getMailOrderSales().getMailOrderSalesUrl();
  }

  public boolean getEnrollmentPassed() {
    return this.enrollmentInfo.getEnrollmentPassed();
  }

  public LocalDateTime getEnrollmentAt() {
    return this.enrollmentInfo.getEnrollmentAt();
  }

  public String updateEscrowService(String escrowServiceUri) {
    this.enrollmentInfo = enrollmentInfo.setEscrowService(escrowServiceUri);
    return this.getEscrowServiceUrl();
  }

  public String updateMailOrderSales(String mailOrderSalesUri) {
    this.enrollmentInfo = enrollmentInfo.setMailOrderSales(mailOrderSalesUri);
    return this.getMailOrderSalesUrl();
  }

  public boolean convertEnrollmentJudgeStatus() {
    this.enrollmentInfo = enrollmentInfo.convertEnrollmentPassedStatus();
    return this.getEnrollmentInfo().getEnrollmentPassed();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Author author = (Author) o;
    return Objects.equals(authorId, author.authorId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(authorId);
  }
}
