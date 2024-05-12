package com.onetuks.dbstorage.member.entity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.dbstorage.global.vo.auth.RoleType;
import com.onetuks.dbstorage.order.vo.ProfileImgFilePath;
import com.onetuks.dbstorage.order.vo.DefaultAddressInfo;
import com.onetuks.dbstorage.order.vo.DefaultCashReceiptInfo;
import com.onetuks.dbstorage.order.vo.Nickname;
import com.onetuks.dbstorage.member.embedded.AuthInfo;
import com.onetuks.dbstorage.order.vo.CashReceiptType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
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
public class MemberEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long memberId;

  @Embedded private AuthInfo authInfo;

  @Column(name = "nickname", unique = true)
  @Embedded private Nickname nickname;

  @Embedded private ProfileImgFilePath profileImgFilePath;

  @Column(name = "alarm_permission", nullable = false)
  private Boolean alarmPermission;

  @Embedded private DefaultAddressInfo defaultAddressInfo;

  @Builder
  public MemberEntity(
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

  public List<RoleType> getRoleTypes() {
    return this.authInfo.getRoleTypes();
  }

  public List<RoleType> grantAuthorRole() {
    this.authInfo = authInfo.addRole(RoleType.AUTHOR);
    return getRoleTypes();
  }

  public List<RoleType> revokeAuthorRole() {
    this.authInfo = authInfo.removeRole(RoleType.AUTHOR);
    return getRoleTypes();
  }

  public MemberEntity changeNickname(String nickname) {
    this.nickname = new Nickname(nickname);
    return this;
  }

  public MemberEntity changeAlarmPermission(boolean alarmPermission) {
    this.alarmPermission = alarmPermission;
    return this;
  }

  public MemberEntity changeProfileImgFile(String profileImgFile) {
    this.profileImgFilePath = new ProfileImgFilePath(profileImgFile);
    return this;
  }

  public MemberEntity changeDefaultAddressInfo(String defaultAddress, String defaultAddressDetail) {
    this.defaultAddressInfo = new DefaultAddressInfo(defaultAddress, defaultAddressDetail);
    return this;
  }

  public MemberEntity changeDefaultCashReceiptInfo(
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
    MemberEntity memberEntity = (MemberEntity) o;
    return Objects.equals(memberId, memberEntity.memberId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(memberId);
  }
}
