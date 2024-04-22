package com.onetuks.goguma_bookstore.author.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.author.model.vo.EnrollmentInfo;
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
      ProfileImgFile profileImgFile,
      String nickname,
      String introduction,
      String instagramUrl,
      EnrollmentInfo enrollmentInfo) {
    this.member = member;
    this.profileImgFile = profileImgFile;
    this.nickname = new Nickname(nickname);
    this.introduction = introduction;
    this.instagramUrl = instagramUrl;
    this.enrollmentInfo =
        Objects.requireNonNullElse(enrollmentInfo, EnrollmentInfo.builder().build());
    this.authorStatics = AuthorStatics.builder().author(this).build();
  }

  public String getNickname() {
    return this.nickname.getNicknameValue();
  }

  public String getProfileImgUrl() {
    return this.profileImgFile.getProfileImgUrl();
  }

  public Author changeProfileImgFile(ProfileImgFile profileImgFile) {
    this.profileImgFile = profileImgFile;
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
