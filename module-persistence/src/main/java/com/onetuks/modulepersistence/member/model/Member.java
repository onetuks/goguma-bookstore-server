package com.onetuks.modulepersistence.member.model;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import com.onetuks.modulepersistence.global.vo.file.ProfileImgFilePath;
import com.onetuks.modulepersistence.global.vo.order.DefaultAddressInfo;
import com.onetuks.modulepersistence.global.vo.order.DefaultCashReceiptInfo;
import com.onetuks.modulepersistence.global.vo.profile.Nickname;
import com.onetuks.modulepersistence.member.vo.AuthInfo;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

  @Embedded private Nickname nickname;

  @Embedded private ProfileImgFilePath profileImgFilePath;

  @Column(name = "alarm_permission", nullable = false)
  private Boolean alarmPermission;

  @Embedded private DefaultAddressInfo defaultAddressInfo;

  @Embedded private DefaultCashReceiptInfo defaultCashReceiptInfo;

  @Builder
  public Member(
      AuthInfo authInfo,
      String nickname,
      String profileImgFilePath,
      Boolean alarmPermission,
      DefaultAddressInfo defaultAddressInfo,
      DefaultCashReceiptInfo defaultCashReceiptInfo) {
    this.authInfo = authInfo;
    this.nickname = new Nickname(nickname);
    this.profileImgFilePath = new ProfileImgFilePath(profileImgFilePath);
    this.alarmPermission = Objects.requireNonNullElse(alarmPermission, true);
    this.defaultAddressInfo = defaultAddressInfo;
    this.defaultCashReceiptInfo = defaultCashReceiptInfo;
  }

  public String getNickname() {
    return this.nickname.getNicknameValue();
  }

  public String getProfileImgUrl() {
    return this.profileImgFilePath.getUrl();
  }

  public String getDefaultAddress() {
    if (this.defaultAddressInfo == null) {
      return null;
    }

    return this.defaultAddressInfo.getDefaultAddress();
  }

  public String getDefaultAddressDetail() {
    if (this.defaultAddressInfo == null) {
      return null;
    }

    return this.defaultAddressInfo.getDefaultAddressDetail();
  }

  public CashReceiptType getDefaultCashReceiptType() {
    return this.defaultCashReceiptInfo.getDefaultCashReceiptType();
  }

  public String getDefaultCashReceiptNumber() {
    return this.defaultCashReceiptInfo.getDefaultCashReceiptNumber();
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

  public Member changeNickname(String nickname) {
    this.nickname = new Nickname(nickname);
    return this;
  }

  public Member changeAlarmPermission(boolean alarmPermission) {
    this.alarmPermission = alarmPermission;
    return this;
  }

  public Member changeProfileImgFile(String profileImgFile) {
    this.profileImgFilePath = new ProfileImgFilePath(profileImgFile);
    return this;
  }

  public Member changeDefaultAddressInfo(String defaultAddress, String defaultAddressDetail) {
    this.defaultAddressInfo = new DefaultAddressInfo(defaultAddress, defaultAddressDetail);
    return this;
  }

  public Member changeDefaultCashReceiptInfo(
      CashReceiptType defaultCashReceiptType, String defaultCashReceiptNumber) {
    this.defaultCashReceiptInfo =
        new DefaultCashReceiptInfo(defaultCashReceiptType, defaultCashReceiptNumber);
    return this;
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
