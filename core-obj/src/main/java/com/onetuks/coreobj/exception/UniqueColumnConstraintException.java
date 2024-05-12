package com.onetuks.coreobj.exception;

import com.onetuks.coreobj.error.ErrorCode;

public class UniqueColumnConstraintException extends IllegalArgumentException {

  public UniqueColumnConstraintException() {
  }

  public UniqueColumnConstraintException(String s) {
    super(s);
  }
}
