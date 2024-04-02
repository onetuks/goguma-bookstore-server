package com.onetuks.goguma_bookstore.author.model;

import static jakarta.persistence.CascadeType.REMOVE;

import com.onetuks.goguma_bookstore.auth.model.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

  @Column(name = "profile_img", nullable = false)
  private String profileImg;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "introduction", nullable = false)
  private String introduction;

  @Column(name = "escrow_service", nullable = false)
  private String escrowService;

  @Column(name = "mail_order_sales", nullable = false)
  private String mailOrderSales;

  @OneToOne(
      mappedBy = "author",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, REMOVE})
  private AuthorStatics authorStatics;

  @Builder
  public Author(
      Member member,
      String profileImg,
      String nickname,
      String introduction,
      String escrowService,
      String mailOrderSales) {
    this.member = member;
    this.profileImg = profileImg;
    this.nickname = nickname;
    this.introduction = introduction;
    this.escrowService = escrowService;
    this.mailOrderSales = mailOrderSales;
    this.authorStatics = AuthorStatics.builder().author(this).build();
  }
}
