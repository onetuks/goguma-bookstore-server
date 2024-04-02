package com.onetuks.goguma_bookstore.auth.exception;

import com.onetuks.goguma_bookstore.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenIsLogoutException extends IllegalStateException {

  private final ErrorCode errorCode;

  public TokenIsLogoutException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }
}