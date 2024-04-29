package com.onetuks.modulereader.member.controller.dto.request;

import com.onetuks.modulereader.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import com.onetuks.modulepersistence.order.vo.CashReceiptType;
import jakarta.validation.constraints.NotBlank;

public record MemberDefaultCashReceiptEditRequest(
    @NotBlank String defaultCashReceiptType, @NotBlank String defaultCashReceiptNumber) {

  public MemberDefaultCashReceiptEditParam to() {
    return new MemberDefaultCashReceiptEditParam(
        CashReceiptType.of(defaultCashReceiptType()), defaultCashReceiptNumber());
  }
}
