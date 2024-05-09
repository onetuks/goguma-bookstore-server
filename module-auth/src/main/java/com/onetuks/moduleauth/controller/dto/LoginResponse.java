package com.onetuks.moduleauth.controller.dto;

import com.onetuks.moduleauth.service.dto.LoginResult;
import com.onetuks.modulepersistence.global.vo.auth.RoleType;
import java.util.List;

public record LoginResponse(
    String appToken, boolean isNewMember, long loginId, String name, List<RoleType> roleTypes) {

  public static LoginResponse from(LoginResult loginResult) {
    return new LoginResponse(
        loginResult.accessToken(),
        loginResult.isNewMember(),
        loginResult.loginId(),
        loginResult.name(),
        loginResult.roleTypes());
  }
}
