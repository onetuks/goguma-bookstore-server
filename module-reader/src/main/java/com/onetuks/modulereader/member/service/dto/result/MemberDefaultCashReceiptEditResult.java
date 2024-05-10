package com.onetuks.modulereader.member.service.dto.result;

import com.onetuks.modulepersistence.member.entity.MemberEntity;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;

public record MemberDefaultCashReceiptEditResult(
    CashReceiptType defaultCashReceiptType, String defaultCashReciptNumber) {

  public static MemberDefaultCashReceiptEditResult from(MemberEntity memberEntity) {
    return new MemberDefaultCashReceiptEditResult(
        memberEntity.getDefaultCashReceiptType(), memberEntity.getDefaultCashReceiptNumber());
  }
}
