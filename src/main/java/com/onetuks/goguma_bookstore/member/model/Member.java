package com.onetuks.goguma_bookstore.member.model;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.goguma_bookstore.member.vo.AuthInfo;
import com.onetuks.goguma_bookstore.member.vo.RoleType;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

  @Embedded private AuthInfo authInfo;

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

  @Column(name = "alarm_permission", nullable = false)
  private Boolean alarmPermission;

  @Builder
  public Member(
      AuthInfo authInfo,
      String nickname,
      String profileImgUri,
      String defaultAddress,
      String defaultAddressDetail,
      CashReceiptType defaultCashReceiptType,
      String defaultCashReceiptNumber,
      Boolean alarmPermission) {
    this.authInfo = authInfo;
    this.nickname = nickname;
    this.profileImgUri = profileImgUri;
    this.defaultAddress = defaultAddress;
    this.defaultAddressDetail = defaultAddressDetail;
    this.defaultCashReceiptType = defaultCashReceiptType;
    this.defaultCashReceiptNumber = defaultCashReceiptNumber;
    this.alarmPermission = alarmPermission;
  }

  public RoleType getRoleType() {
    return this.authInfo.getRoleType();
  }

  public RoleType grantAuthorRole() {
    this.authInfo = authInfo.changeRole(RoleType.AUTHOR);
    return getRoleType();
  }

  public RoleType revokeAuthorRole() {
    this.authInfo = authInfo.changeRole(RoleType.USER);
    return getRoleType();
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
