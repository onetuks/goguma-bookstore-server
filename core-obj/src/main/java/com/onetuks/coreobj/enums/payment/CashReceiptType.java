package com.onetuks.coreobj.enums.payment;

public enum CashReceiptType {
  PERSON,
  COMPANY;

  public static CashReceiptType of(String type) {
    return CashReceiptType.valueOf(type.toUpperCase());
  }
}
