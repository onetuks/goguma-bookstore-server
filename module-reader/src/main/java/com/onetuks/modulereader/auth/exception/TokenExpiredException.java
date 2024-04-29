package com.onetuks.modulereader.auth.exception;

import com.onetuks.modulepersistence.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenExpiredException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenExpiredException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
