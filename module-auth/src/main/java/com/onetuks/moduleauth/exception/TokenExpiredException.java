package com.onetuks.moduleauth.exception;

import com.onetuks.modulecommon.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenExpiredException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenExpiredException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}