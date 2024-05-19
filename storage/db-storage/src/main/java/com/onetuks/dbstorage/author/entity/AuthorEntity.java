package com.onetuks.dbstorage.author.entity;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.coreobj.annotation.Generated;
import com.onetuks.dbstorage.member.entity.MemberEntity;
import jakarta.persistence.Column;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "authors")
public class AuthorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "author_id", nullable = false)
  private Long authorId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", unique = true)
  private MemberEntity memberEntity;

  @Column(name = "profile_img_uri", nullable = false)
  private String profileImgUri;

  @Column(name = "nickname", nullable = false, unique = true)
  private String nickname;

  @Column(name = "introduction", nullable = false)
  private String introduction;

  @Column(name = "instagram_url", nullable = false, unique = true)
  private String instagramUrl;

  @Column(name = "business_number", nullable = false, unique = true)
  private String businessNumber;

  @Column(name = "mail_order_sales_number", nullable = false, unique = true)
  private String mailOrderSalesNumber;

  @Column(name = "is_enrollment_passed", nullable = false)
  private Boolean isEnrollmentPassed;

  @Column(name = "enrollment_at", nullable = false)
  private LocalDateTime enrollmentAt;

  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {PERSIST, REMOVE})
  @JoinColumn(name = "author_statics_id", unique = true, nullable = false)
  private AuthorStaticsEntity authorStaticsEntity;

  public AuthorEntity(
      Long authorId,
      MemberEntity memberEntity,
      String profileImgUri,
      String nickname,
      String introduction,
      String instagramUrl,
      String businessNumber,
      String mailOrderSalesNumber,
      Boolean isEnrollmentPassed,
      LocalDateTime enrollmentAt,
      AuthorStaticsEntity authorStaticsEntity) {
    this.authorId = authorId;
    this.memberEntity = memberEntity;
    this.profileImgUri = profileImgUri;
    this.nickname = nickname;
    this.introduction = introduction;
    this.instagramUrl = instagramUrl;
    this.businessNumber = businessNumber;
    this.mailOrderSalesNumber = mailOrderSalesNumber;
    this.isEnrollmentPassed = isEnrollmentPassed;
    this.enrollmentAt = enrollmentAt;
    this.authorStaticsEntity =
        authorStaticsEntity != null ? authorStaticsEntity : AuthorStaticsEntity.init();
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
    AuthorEntity authorEntity = (AuthorEntity) o;
    return Objects.equals(authorId, authorEntity.authorId);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hashCode(authorId);
  }
}
