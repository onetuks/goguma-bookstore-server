package com.onetuks.corevo.vo.payment;

public enum CashReceiptType {
  PERSON,
  COMPANY;

  public static CashReceiptType of(String type) {
    return CashReceiptType.valueOf(type.toUpperCase());
  }
}
