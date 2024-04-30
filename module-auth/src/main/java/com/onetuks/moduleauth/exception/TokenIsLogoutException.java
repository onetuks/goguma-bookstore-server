package com.onetuks.moduleauth.exception;

import com.onetuks.modulecommon.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenIsLogoutException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenIsLogoutException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
