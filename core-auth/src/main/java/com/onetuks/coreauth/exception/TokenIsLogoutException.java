package com.onetuks.coreauth.exception;

import com.onetuks.coreobj.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenIsLogoutException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenIsLogoutException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
