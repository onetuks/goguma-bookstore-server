package com.onetuks.goguma_bookstore.member.service.dto.result;

import com.onetuks.goguma_bookstore.member.model.Member;
import com.onetuks.goguma_bookstore.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditResult(
    CashReceiptType defaultCashReceiptType, String defaultCashReciptNumber) {

  public static MemberDefaultCashReceiptEditResult from(Member member) {
    return new MemberDefaultCashReceiptEditResult(
        member.getDefaultCashReceiptType(), member.getDefaultCashReceiptNumber());
  }
}
