package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.model.Member;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditResult(
    CashReceiptType defaultCashReceiptType, String defaultCashReciptNumber) {

  public static MemberDefaultCashReceiptEditResult from(Member member) {
    return new MemberDefaultCashReceiptEditResult(
        member.getDefaultCashReceiptType(), member.getDefaultCashReceiptNumber());
  }
}
