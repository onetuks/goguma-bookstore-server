package com.onetuks.coreauth.exception;

import com.onetuks.coreobj.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenExpiredException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenExpiredException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
