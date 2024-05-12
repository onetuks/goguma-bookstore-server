package com.onetuks.dbstorage.member.embedded;

import static jakarta.persistence.EnumType.STRING;

import com.onetuks.dbstorage.global.vo.auth.ClientProvider;
import com.onetuks.dbstorage.global.vo.auth.RoleType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

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

  @Type(JsonType.class)
  @Column(name = "role_types", nullable = false)
  private List<RoleType> roleTypes;

  @Builder
  public AuthInfo(
      String name, String socialId, ClientProvider clientProvider, List<RoleType> roleTypes) {
    this.name = name;
    this.socialId = socialId;
    this.clientProvider = clientProvider;
    this.roleTypes = roleTypes;
  }

  public AuthInfo addRole(RoleType roleType) {
    List<RoleType> newRoleTypes = new ArrayList<>(this.roleTypes);
    newRoleTypes.add(roleType);

    return new AuthInfo(this.name, this.socialId, this.clientProvider, newRoleTypes);
  }

  public AuthInfo removeRole(RoleType roleType) {
    List<RoleType> newRoleTypes = new ArrayList<>(this.roleTypes);
    newRoleTypes.remove(roleType);

    return new AuthInfo(this.name, this.socialId, this.clientProvider, newRoleTypes);
  }
}
