package com.onetuks.goguma_bookstore.author.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.auth.model.Member;
import com.onetuks.goguma_bookstore.author.model.vo.EscrowService;
import com.onetuks.goguma_bookstore.author.model.vo.MailOrderSales;
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

  @Embedded private EscrowService escrowService;

  @Embedded private MailOrderSales mailOrderSales;

  @Column(name = "enroll_passed", nullable = false)
  private Boolean enrollPassed;

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
      String escrowServiceUri,
      String mailOrderSalesUri,
      Boolean enrollPassed) {
    this.member = member;
    this.profileImg = new ProfileImg(profileImgUri);
    this.nickname = nickname;
    this.introduction = introduction;
    this.escrowService = new EscrowService(escrowServiceUri);
    this.mailOrderSales = new MailOrderSales(mailOrderSalesUri);
    this.enrollPassed = Objects.requireNonNullElse(enrollPassed, Boolean.FALSE);
    this.authorStatics = AuthorStatics.builder().author(this).build();
  }

  public String getProfileImgUrl() {
    return this.profileImg.getProfileImgUrl();
  }

  public String getEscrowServiceUrl() {
    return this.escrowService.getEscrowServiceUrl();
  }

  public String getMailOrderSalesUrl() {
    return this.mailOrderSales.getMailOrderSalesUrl();
  }

  public void handOverEscrowService(String escrowServiceUri) {
    this.escrowService = new EscrowService(escrowServiceUri);
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
