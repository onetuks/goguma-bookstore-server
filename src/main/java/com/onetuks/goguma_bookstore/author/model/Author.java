package com.onetuks.goguma_bookstore.author.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
import com.onetuks.goguma_bookstore.global.vo.file.EscrowServiceFile;
import com.onetuks.goguma_bookstore.global.vo.file.MailOrderSalesFile;
import com.onetuks.goguma_bookstore.global.vo.file.ProfileImgFile;
import com.onetuks.goguma_bookstore.global.vo.profile.Nickname;
import com.onetuks.goguma_bookstore.member.model.Member;
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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "member_id", unique = true)
  private Member member;

  @Embedded private ProfileImgFile profileImgFile;

  @Column(name = "nickname", nullable = false)
  private Nickname nickname;

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
      ProfileImgFile profileImgFile,
      String nickname,
      String introduction,
      EnrollmentInfo enrollmentInfo) {
    this.member = member;
    this.profileImgFile = profileImgFile;
    this.nickname = new Nickname(nickname);
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

  public String getNickname() {
    return this.nickname.getNicknameValue();
  }

  public String getProfileImgUrl() {
    return this.profileImgFile.getProfileImgUrl();
  }

  public String getEscrowServiceUrl() {
    EscrowServiceFile escrowServiceFile = this.enrollmentInfo.getEscrowServiceFile();
    if (escrowServiceFile == null) {
      return null;
    }

    return escrowServiceFile.getEscrowServiceUrl();
  }

  public String getMailOrderSalesUrl() {
    MailOrderSalesFile mailOrderSalesFile = this.enrollmentInfo.getMailOrderSalesFile();
    if (mailOrderSalesFile == null) {
      return null;
    }
    return mailOrderSalesFile.getMailOrderSalesUrl();
  }

  public boolean getEnrollmentPassed() {
    return this.enrollmentInfo.getEnrollmentPassed();
  }

  public LocalDateTime getEnrollmentAt() {
    return this.enrollmentInfo.getEnrollmentAt();
  }

  public Author updateProfileImgFile(ProfileImgFile profileImgFile) {
    this.profileImgFile = profileImgFile;
    return this;
  }

  public Author updateNickname(String nickname) {
    this.nickname = new Nickname(nickname);
    return this;
  }

  public Author updateIntroduction(String introduction) {
    this.introduction = introduction;
    return this;
  }

  public String updateEscrowService(EscrowServiceFile escrowServiceFile) {
    this.enrollmentInfo = enrollmentInfo.setEscrowServiceFile(escrowServiceFile);
    return this.getEscrowServiceUrl();
  }

  public String updateMailOrderSales(MailOrderSalesFile mailOrderSalesFile) {
    this.enrollmentInfo = enrollmentInfo.setMailOrderSalesFile(mailOrderSalesFile);
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
