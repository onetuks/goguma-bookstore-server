package com.onetuks.coreauth.exception;

import com.onetuks.coreobj.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenValidFailedException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenValidFailedException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
