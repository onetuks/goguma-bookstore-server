package com.onetuks.modulecommon.exception;

import com.onetuks.modulecommon.error.ErrorCode;

public class ApiAccessDeniedException extends IllegalArgumentException {

  private final ErrorCode errorCode;

  public ApiAccessDeniedException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
