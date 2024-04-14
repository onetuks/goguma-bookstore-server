package com.onetuks.goguma_bookstore.member.controller.dto.response;

import com.onetuks.goguma_bookstore.member.service.dto.result.MemberDefaultCashReceiptEditResult;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditResponse(
    CashReceiptType defaultCashReceiptType, String defaultCashReceiptNumber) {

  public static MemberDefaultCashReceiptEditResponse from(
      MemberDefaultCashReceiptEditResult result) {
    return new MemberDefaultCashReceiptEditResponse(
        result.defaultCashReceiptType(), result.defaultCashReciptNumber());
  }
}
