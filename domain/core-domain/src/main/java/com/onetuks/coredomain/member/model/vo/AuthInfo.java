package com.onetuks.coredomain.member.model.vo;

import com.onetuks.coreobj.enums.member.ClientProvider;
import com.onetuks.coreobj.enums.member.RoleType;
import java.util.ArrayList;
import java.util.List;

public record AuthInfo(
    String name,
    String socialId,
    ClientProvider clientProvider,
    List<RoleType> roles
) {

  public AuthInfo removeAuthorRole() {
    List<RoleType> notContainsAuthorRoles = roles.stream()
        .filter(roleType -> !roleType.equals(RoleType.AUTHOR))
        .toList();

    return new AuthInfo(name, socialId, clientProvider, notContainsAuthorRoles);
  }

  public AuthInfo grantAuthorRole() {
    List<RoleType> containsAuthorRoles = new ArrayList<>(roles);
    containsAuthorRoles.add(RoleType.AUTHOR);

    return new AuthInfo(name, socialId, clientProvider, containsAuthorRoles);
  }
}
