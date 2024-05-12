package com.onetuks.readerapi.controller.dto.request;

import com.onetuks.dbstorage.order.vo.CashReceiptType;
import com.onetuks.modulereader.member.service.dto.param.MemberDefaultCashReceiptEditParam;
import jakarta.validation.constraints.NotBlank;

public record MemberDefaultCashReceiptEditRequest(
    @NotBlank String defaultCashReceiptType, @NotBlank String defaultCashReceiptNumber) {

  public MemberDefaultCashReceiptEditParam to() {
    return new MemberDefaultCashReceiptEditParam(
        CashReceiptType.of(defaultCashReceiptType()), defaultCashReceiptNumber());
  }
}
