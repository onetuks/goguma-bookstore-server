package com.onetuks.goguma_bookstore.auth.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.goguma_bookstore.auth.vo.ClientProvider;
import com.onetuks.goguma_bookstore.auth.vo.RoleType;
import com.onetuks.goguma_bookstore.order.model.CashReceiptType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(
    name = "members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"social_id", "client_provider"}))
public class Member {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long memberId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "social_id", nullable = false)
  private String socialId;

  @Enumerated(value = STRING)
  @Column(name = "client_provider", nullable = false)
  private ClientProvider clientProvider;

  @Enumerated(value = STRING)
  @Column(name = "role_type", nullable = false)
  private RoleType roleType;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "profile_img")
  private String profileImg;

  @Column(name = "default_address")
  private String defaultAddress;

  @Column(name = "default_address_detail")
  private String defaultAddressDetail;

  @Enumerated(value = STRING)
  @Column(name = "default_cash_receipt_type")
  private CashReceiptType defaultCashReceiptType;

  @Column(name = "default_cash_receipt_number")
  private String defaultCashReceiptNumber;

  @Builder
  public Member(
      String name,
      String socialId,
      ClientProvider clientProvider,
      RoleType roleType,
      String nickname,
      String profileImg,
      String defaultAddress,
      String defaultAddressDetail,
      CashReceiptType defaultCashReceiptType,
      String defaultCashReceiptNumber) {
    this.name = name;
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roleType = roleType;
    this.nickname = nickname;
    this.profileImg = profileImg;
    this.defaultAddress = defaultAddress;
    this.defaultAddressDetail = defaultAddressDetail;
    this.defaultCashReceiptType = defaultCashReceiptType;
    this.defaultCashReceiptNumber = defaultCashReceiptNumber;
  }
}
