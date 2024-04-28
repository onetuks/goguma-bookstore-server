package com.onetuks.modulepersistence.member.vo;

import static jakarta.persistence.EnumType.STRING;

import com.onetuks.modulepersistence.global.vo.auth.ClientProvider;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AuthInfo {

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

  public AuthInfo(String name, String socialId, ClientProvider clientProvider, RoleType roleType) {
    this.name = name;
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roleType = roleType;
  }

  public static AuthInfo from(UserData userData) {
    return new AuthInfo(
        userData.name(), userData.socialId(), userData.clientProvider(), userData.roleType());
  }

  public AuthInfo changeRole(RoleType roleType) {
    return new AuthInfo(this.name, this.socialId, this.clientProvider, roleType);
  }
}
