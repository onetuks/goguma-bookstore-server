package com.onetuks.goguma_bookstore.member.controller.dto.request;

import com.onetuks.goguma_bookstore.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditRequest(
    String defaultCashReceiptType, String defaultCashReceiptNumber) {

  public MemberDefaultCashReceiptEditParam to() {
    return new MemberDefaultCashReceiptEditParam(
        CashReceiptType.of(defaultCashReceiptType()), defaultCashReceiptNumber());
  }
}
