package com.onetuks.goguma_bookstore.auth.model.vo;

import lombok.Getter;

@Getter
public enum RoleType {
  USER("ROLE_USER"),
  AUTHOR("ROLE_AUTHOR"),
  ADMIN("ROLE_ADMIN");

  private final String roleName;

  RoleType(String roleName) {
    this.roleName = roleName;
  }
}
