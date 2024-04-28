package com.onetuks.modulepersistence.author.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.modulepersistence.global.vo.file.ProfileImgFilePath;
import com.onetuks.modulepersistence.global.vo.profile.Nickname;
import com.onetuks.modulepersistence.author.model.embedded.EnrollmentInfo;
import com.onetuks.modulepersistence.member.model.Member;
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

  @OneToOne(fetch = FetchType.LAZY, cascade = REMOVE)
  @JoinColumn(name = "member_id", unique = true)
  private Member member;

  @Embedded
  private ProfileImgFilePath profileImgFilePath;

  @Column(nullable = false)
  @Embedded
  private Nickname nickname;

  @Column(name = "introduction", nullable = false)
  private String introduction;

  @Column(name = "instagram_url", nullable = false, unique = true)
  private String instagramUrl;

  @Embedded private EnrollmentInfo enrollmentInfo;

  @OneToOne(
      mappedBy = "author",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private AuthorStatics authorStatics;

  @Builder
  public Author(
      Member member,
      String profileImgFilePath,
      String nickname,
      String introduction,
      String instagramUrl,
      EnrollmentInfo enrollmentInfo) {
    this.member = member;
    this.profileImgFilePath = new ProfileImgFilePath(profileImgFilePath);
    this.nickname = new Nickname(nickname);
    this.introduction = introduction;
    this.instagramUrl = instagramUrl;
    this.enrollmentInfo = Objects.requireNonNullElse(enrollmentInfo, EnrollmentInfo.init());
    this.authorStatics = AuthorStatics.builder().author(this).build();
  }

  public String getNickname() {
    return this.nickname.getNicknameValue();
  }

  public String getProfileImgUrl() {
    return this.profileImgFilePath.getUrl();
  }

  public String getBusinessNumber() {
    return this.enrollmentInfo.getBusinessNumber();
  }

  public String getMailOrderSalesNumber() {
    return this.enrollmentInfo.getMailOrderSalesNumber();
  }

  public boolean getEnrollmentPassed() {
    return this.enrollmentInfo.getEnrollmentPassed();
  }

  public LocalDateTime getEnrollmentAt() {
    return this.enrollmentInfo.getEnrollmentAt();
  }

  public long getSubscribeCount() {
    return this.authorStatics.getSubscribeCount();
  }

  public long getBookCount() {
    return this.authorStatics.getBookCount();
  }

  public long getRestockCount() {
    return this.authorStatics.getRestockCount();
  }

  public Author changeProfileImgPath(String profileImgFilePath) {
    this.profileImgFilePath = new ProfileImgFilePath(profileImgFilePath);
    return this;
  }

  public Author changeAuthorProfile(String nickname, String introduction, String instagramUrl) {
    this.nickname = new Nickname(nickname);
    this.introduction = introduction;
    this.instagramUrl = instagramUrl;
    return this;
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
