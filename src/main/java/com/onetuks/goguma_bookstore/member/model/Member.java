package com.onetuks.goguma_bookstore.member.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.goguma_bookstore.member.vo.ClientProvider;
import com.onetuks.goguma_bookstore.member.vo.RoleType;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
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

  @Column(name = "profile_img_uri")
  private String profileImgUri;

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
      String profileImgUri,
      String defaultAddress,
      String defaultAddressDetail,
      CashReceiptType defaultCashReceiptType,
      String defaultCashReceiptNumber) {
    this.name = name;
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roleType = roleType;
    this.nickname = nickname;
    this.profileImgUri = profileImgUri;
    this.defaultAddress = defaultAddress;
    this.defaultAddressDetail = defaultAddressDetail;
    this.defaultCashReceiptType = defaultCashReceiptType;
    this.defaultCashReceiptNumber = defaultCashReceiptNumber;
  }

  public RoleType grantAuthorRole() {
    this.roleType = RoleType.AUTHOR;
    return this.roleType;
  }

  public RoleType revokeAuthorRole() {
    this.roleType = RoleType.USER;
    return this.roleType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Member member = (Member) o;
    return Objects.equals(memberId, member.memberId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(memberId);
  }
}
