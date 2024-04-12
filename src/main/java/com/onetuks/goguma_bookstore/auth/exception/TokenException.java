package com.onetuks.goguma_bookstore.auth.exception;

import com.onetuks.goguma_bookstore.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
