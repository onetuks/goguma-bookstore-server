package com.onetuks.coreobj.exception;

public class UniqueColumnConstraintException extends IllegalArgumentException {

  public UniqueColumnConstraintException() {}

  public UniqueColumnConstraintException(String s) {
    super(s);
  }
}
