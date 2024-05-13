package com.onetuks.dbstorage.member.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "social_id", nullable = false)
  private String socialId;

  @Enumerated(value = STRING)
  @Column(name = "client_provider", nullable = false)
  private ClientProvider clientProvider;

  @Type(JsonType.class)
  @Column(name = "roles", nullable = false)
  private List<RoleType> roles;

  @Column(name = "nickname", unique = true)
  private String nickname;

  @Column(name = "profile_img_uri", nullable = false)
  private String profileImgUri;

  @Column(name = "is_alarm_permitted", nullable = false)
  private Boolean isAlarmPermitted;

  @Column(name = "default_address")
  private String defaultAddress;

  @Column(name = "default_address_detail")
  private String defaultAddressDetail;

  public MemberEntity(
      Long memberId,
      String name,
      String socialId,
      ClientProvider clientProvider,
      List<RoleType> roles,
      String nickname,
      String profileImgUri,
      Boolean isAlarmPermitted,
      String defaultAddress,
      String defaultAddressDetail) {
    this.memberId = memberId;
    this.name = name;
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roles = roles;
    this.nickname = nickname;
    this.profileImgUri = profileImgUri;
    this.isAlarmPermitted = isAlarmPermitted;
    this.defaultAddress = defaultAddress;
    this.defaultAddressDetail = defaultAddressDetail;
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
