package com.onetuks.coreauth.service.dto;

import com.onetuks.dbstorage.global.vo.auth.RoleType;
import java.util.List;

public record LoginResult(
    String accessToken, boolean isNewMember, long loginId, String name, List<RoleType> roleTypes) {

  public static LoginResult of(
      String accessToken,
      boolean isNewMember,
      Long loginId,
      String name,
      List<RoleType> roleTypes) {
    return new LoginResult(accessToken, isNewMember, loginId, name, roleTypes);
  }
}
