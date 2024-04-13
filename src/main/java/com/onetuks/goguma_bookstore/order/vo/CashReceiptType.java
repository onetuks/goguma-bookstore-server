package com.onetuks.goguma_bookstore.order.vo;

public enum CashReceiptType {
  PERSON,
  COMPANY;

  public static CashReceiptType of(String type) {
    return CashReceiptType.valueOf(type.toUpperCase());
  }
}
