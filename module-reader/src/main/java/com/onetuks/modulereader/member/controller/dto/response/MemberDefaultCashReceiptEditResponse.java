package com.onetuks.modulereader.member.controller.dto.response;

import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import com.onetuks.modulereader.member.service.dto.result.MemberDefaultCashReceiptEditResult;

public record MemberDefaultCashReceiptEditResponse(
    CashReceiptType defaultCashReceiptType, String defaultCashReceiptNumber) {

  public static MemberDefaultCashReceiptEditResponse from(
      MemberDefaultCashReceiptEditResult result) {
    return new MemberDefaultCashReceiptEditResponse(
        result.defaultCashReceiptType(), result.defaultCashReciptNumber());
  }
}